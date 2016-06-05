package com.broker.utils.jms;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.stereotype.Component;

import com.broker.entity.Order;
import com.broker.service.OrderService;

@Component
public class QueueReceiver implements MessageListener {
	@Resource
	private OrderService orderService;
	
    @Override
    public void onMessage(Message message) {
        try {
        	ActiveMQObjectMessage objMsg = (ActiveMQObjectMessage) message;
        	Order order = (Order)objMsg.getObject();
            System.out.println("QueueReceiver接收到消息:" + order.getProduct());
            
            if (order.getOrderType().equals("market")) {
    			double marketDepth = orderService.dealMarketOrder(order);
    			if (marketDepth != 0.0) {
    				order.setPrice(marketDepth);
    				orderService.dealStopOrder(order);
    			}
    		}
    		else if (order.getOrderType().equals("limit")){
    			double marketDepth = orderService.dealLimitOrder(order);
    			if (marketDepth != 0.0) {
    				order.setPrice(marketDepth);
    				orderService.dealStopOrder(order);
    			}
    		}
    		else if (order.getOrderType().equals("stop")) {
    			orderService.addStopOrder(order);
    		}
    		else if (order.getOrderType().equals("cancel")) {
    			
    			orderService.dealCancelOrder(order);
    		}
    		if (order.getOrderType().equals("add")) {
    			orderService.addOrder(order);
    		}
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
 
}