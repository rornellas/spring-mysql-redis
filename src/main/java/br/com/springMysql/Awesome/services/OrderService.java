package br.com.springMysql.Awesome.services;

import br.com.springMysql.Awesome.Repository.CostumerRepository;
import br.com.springMysql.Awesome.Repository.OrderRepository;
import br.com.springMysql.Awesome.model.Costumer;
import br.com.springMysql.Awesome.model.Order;
import br.com.springMysql.Awesome.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.json.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private ProductService productService;

    @Autowired
    public OrderService(OrderRepository orderRepository) {this.orderRepository = orderRepository;}

    @CacheEvict(value = "order", key = "#order.getId()")
    public Order createOrder (Order order){
        System.out.println(order);
        List<Product> listProducts = order.getProduct();
        for(Product item : listProducts){
            System.out.println("----------"+ item.getId());
            System.out.println("------------" + item.getId());

        }

        Order result = null;
        if(!orderRepository.exists(order.getId())) {
            result = this.orderRepository.save(order);
        }
        return result;
    }

    @CachePut(value ="order", key = "#id")
    public Order updateOrder(Long id, Order order) {
        Order result = null;
        if(orderRepository.exists(order.getId())) {
            result = this.orderRepository.save(order);
        }
        return result;
    }

    @CacheEvict(value = "listOrder", key = "#id")
    public boolean deleteOrder(Long id) {
        boolean deleted = false;

        if(orderRepository.exists(id)) {
            this.orderRepository.delete(id);
            deleted = true;
        }
        return deleted;
    }

    @Cacheable(value = "Order")
    public Order getOrder(Long id) {
        return this.orderRepository.findOne(id);
    }

    @CacheEvict(value = "listOrders")
    public Iterable<Order> listAllOrders(){
        return orderRepository.findAll();
    }
}
