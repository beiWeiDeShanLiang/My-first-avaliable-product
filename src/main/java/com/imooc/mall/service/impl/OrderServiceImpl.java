package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.dao.OrderItemMapper;
import com.imooc.mall.model.dao.OrderMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Order;
import com.imooc.mall.model.pojo.OrderItem;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.service.CartService;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.OrderService;
import com.imooc.mall.service.UserService;
import com.imooc.mall.utils.OrderCodeFactory;
import com.imooc.mall.utils.QRcodeGenerator;
import com.imooc.mall.vo.CartVO;
import com.imooc.mall.vo.OrderItemVO;
import com.imooc.mall.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/26日 20:33
 * ----------------------
 * ---------类描述--------
 **/
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CartService cartService;

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    UserService userService;
    @Value("${file.upload.ip}")
    String ip;

    public static ThreadLocal allTotalPriceThread = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            super.initialValue();

            return 0;
        }
    };

    //数据库事务
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(CreateOrderReq createOrderReq) {
        //拿到用户ID，从购物车查找已勾选的商品，若已勾选为空则报错
        Integer userId = UserFilter.currentUser.getId();
        List<CartVO> list = cartService.list(userId);
        List<CartVO> cartVOSTemp = new ArrayList<>();
        for (CartVO cartVO : list) {
            if (Constant.Cart.select.equals(cartVO.getSelected().toString())) {
                cartVOSTemp.add(cartVO);
            }
        }
        if (CollectionUtils.isEmpty(cartVOSTemp)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CART_EMPTY);
        }
        //判断上下架状态，库存
        validSaleStatusAndStock(cartVOSTemp);
        //吧购物车对象转为订单item对象
        List<OrderItem> orderItems = cartVoListToItemList(cartVOSTemp);
        //扣库存，检验库存，
        Integer allTotalPrice = (Integer) allTotalPriceThread.get();
        for (OrderItem orderItem : orderItems) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            int i = product.getStock() - orderItem.getQuantity();
            if (i < 1) {
                throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
            }
            product.setStock(i);
            productMapper.updateByPrimaryKeySelective(product);
            allTotalPrice += orderItem.getTotalPrice();


        }
        //删除购物车已存在的商品
        cleanCartItem(cartVOSTemp);
        //生成订单号
        Order order = new Order();
        String orderNO = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderNO);
        order.setUserId(userId);
        order.setTotalPrice(allTotalPrice);
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAID.getCode());
        order.setPostage(0);
        order.setPaymentType(1);
        orderMapper.insertSelective(order);
        //循环保存商品item
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderNo(orderNO);
            orderItemMapper.insertSelective(orderItem);


        }
        return orderNO;

    }

    private void cleanCartItem(List<CartVO> cartVOSTemp) {
        for (CartVO cartVO : cartVOSTemp) {
            cartService.delete(UserFilter.currentUser.getId(), cartVO.getProductId());
        }


    }

    //    public static void main(String[] args) {
//        int i1=12;
//        int i2=22;
//        System.out.println(i1-i2);
//    }
    private List<OrderItem> cartVoListToItemList(List<CartVO> cartVOSTemp) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartVO cartVO : cartVOSTemp) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            //记录商品快照信息
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItems.add(orderItem);
        }
        return orderItems;
    }


    private void validSaleStatusAndStock(List<CartVO> cartVOs) {
        for (CartVO cartVO : cartVOs) {
            Integer productId = cartVO.getProductId();

            Product product = productMapper.selectByPrimaryKey(productId);
            //判断商品是否存在且上架
            if (product == null || !product.getStatus().equals(Constant.SALE_STATUS)) {
                throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ON_SALE);
            }
            if (cartVO.getQuantity() > product.getStock()) {
                throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);

            }
        }
    }

    @Override
    public OrderVO detail(String orderNum) {

        Order order = orderMapper.selectByOrderNum(orderNum);
        if (StringUtils.isEmpty(order)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_EXISTENT);
        }
        Integer userId = UserFilter.currentUser.getId();
        if (!userId.equals(order.getUserId())) {
            throw new ImoocMallException(ImoocMallExceptionEnum.ORDER_NOT_EXIST);

        }
        OrderVO orderVO = getOrderVO(order);
        return orderVO;
    }

    private OrderVO getOrderVO(Order order) {

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        //获取对应的orderItemList
        List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(order.getOrderNo());
        ArrayList<OrderItemVO> orderItemVOS = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            orderItemVOS.add(orderItemVO);
        }
        orderVO.setOrderItemVOS(orderItemVOS);
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(orderVO.getOrderStatus()).getValue());
        return orderVO;

    }

    @Override
    public PageInfo listForCustom(Integer pageNum, Integer pageSize) {
        Integer userId = UserFilter.currentUser.getId();
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderMapper.selectForCustom(userId);
        List<OrderVO> orderVOList = ordersToOrderVOList(orders);

        PageInfo orderVOPageInfo = new PageInfo<>(orders);
        orderVOPageInfo.setList(orderVOList);

        return orderVOPageInfo;
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderMapper.selectForAdmin();
        List<OrderVO> orderVOList = ordersToOrderVOList(orders);

        PageInfo orderVOPageInfo = new PageInfo<>(orders);
        orderVOPageInfo.setList(orderVOList);

        return orderVOPageInfo;
    }

    private List<OrderVO> ordersToOrderVOList(List<Order> orders) {
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Order order : orders) {
            OrderVO orderVO = getOrderVO(order);
            orderVOList.add(orderVO);

        }
        return orderVOList;
    }


    @Override
    public void cancel(String orderNum) {
        Order order = orderMapper.selectByOrderNum(orderNum);
        //查不到订单报错
        if (order == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.ORDER_NOT_EXIST);
        }
        Integer userId = UserFilter.currentUser.getId();
        if (!userId.equals(order.getUserId())) {
            throw new ImoocMallException(ImoocMallExceptionEnum.ORDER_NOT_EXIST);

        }
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAID.getCode())) {
            order.setOrderStatus(Constant.OrderStatusEnum.CANCELED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKey(order);
        } else {
            //抛出错误操作
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_ORDER_STATUS);
        }

    }


    @Override
    public String codeQR(String orderNo) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String address = ip + ":" + request.getLocalPort();
        String payUrl = "http://" + address + "/pay?orderNo=" + orderNo;
        try {
            QRcodeGenerator.generateQRCodeImage(payUrl, 350, 350, Constant.File_upload_dir +
                    orderNo + ".png");
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String pngAddress = "http://" + address + "/images/" + orderNo + ".png";
        return pngAddress;
    }

    @Override
    public void pay(String orderNo) {
        Order order = orderMapper.selectByOrderNum(orderNo);
        //查不到订单报错
        if (order == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.ORDER_NOT_EXIST);
        }
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAID.getCode())) {
            //省去了支付内容
            order.setOrderStatus(Constant.OrderStatusEnum.PAID.getCode());
            order.setPayTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }


    @Override
    public void deliver(String orderNo) {
        Order order = orderMapper.selectByOrderNum(orderNo);
        //查不到订单报错
        if (order == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.ORDER_NOT_EXIST);
        }
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.PAID.getCode())) {
            //省去了支付内容
            order.setOrderStatus(Constant.OrderStatusEnum.DELIVERED.getCode());
            order.setPayTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }


    @Override
    public void finish(String orderNo) {
        Order order = orderMapper.selectByOrderNum(orderNo);
        //查不到订单报错
        if (order == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.ORDER_NOT_EXIST);
        }

        if (!userService.checkAdminRole(UserFilter.currentUser ) && !order.getUserId().equals(UserFilter.currentUser.getId())) {
            throw new ImoocMallException(ImoocMallExceptionEnum.ORDER_NOT_EXIST);

        }
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.DELIVERED.getCode())) {
            //省去了支付内容
            order.setOrderStatus(Constant.OrderStatusEnum.FINISHED .getCode());
            order.setPayTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

}
