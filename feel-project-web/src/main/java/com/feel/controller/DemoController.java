package com.feel.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by liyl on 2017/4/25.
 */
@Controller
@RequestMapping(value = {"demo"})
public class DemoController {

    private static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @RequestMapping(value = {"","index"},method = RequestMethod.GET)
    public String index(){
        return "demo";
    }

    @RequestMapping(value = {"","index"}, method = RequestMethod.POST)
    public String post(final int category, final int messageConfigId, final Date sentTime, @RequestParam final Map<String, String> map, Model model) {
        final String targets = map.remove("targets");
        if (targets.length() > 1) {
            // 启动线程
            Runnable runnable = runner(category, messageConfigId, sentTime, map, targets);
            new Thread(runnable).start();
        }

        model.addAttribute("status", "success");
        return "redirect:";
    }

    Runnable runner(final int category, final int messageConfigId, final Date sentTime, final Map<String, String> map, final String targets) {
        // 直接扔到后台一个线程里去添加定时消息，即时返回表单提交页面。
        return new Runnable() {
            @Override
            public void run() {
                logger.info("message category is {}", category);
                logger.info("message config id is {}", messageConfigId);
                logger.info("message sent time is {}", sentTime);
                Iterator<String> iterator = Splitter.on("\n").trimResults().omitEmptyStrings().split(targets).iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    Iterator<String> idIterator = Splitter.onPattern("\\s+|\\t").trimResults().omitEmptyStrings().split(next).iterator();
                    ArrayList<String> parts = Lists.newArrayList(idIterator);
                    Map<String, String> customerMap;
                    if (parts.size() > 1) {
                        customerMap = new HashMap<>();
                        // 第一列必须是customerId or wxOpenid
                        next = parts.get(0);
                        Set<String> keySet = map.keySet();
                        for (String key : keySet) {
                            String value = map.get(key);
                            if (value.contains("$[")) {
                                for (int i = 1; i < parts.size(); i++) {
                                    String part = parts.get(i);
                                    value = value.replace("$[" + i + "]", part);
                                }
                            }
                            customerMap.put(key, value);
                        }
                    } else {
                        customerMap = map;
                    }

                    System.out.println("thread process success");

                    /*BaseMessage baseMessage = new BaseMessage();
                    Map<String, String> extra = new HashMap<>(customerMap);
                    if (messageConfigId != Constants.MESSAGE_WECHAT_EMPTY_TEMPLATE) {
                        // 模板消息
                        extra.put(BaseMessage.TEMPLATE_STR, BaseMessage.TEMPLATE_STR);
                    }
                    extra.put("target", next);
                    baseMessage.setExtra(extra);
                    baseMessage.setSendTime(sentTime);
                    baseMessage.setMessageConfigId(messageConfigId);
                    baseMessage.setCategory(category);
                    baseMessage.setOrderId(0l);
                    Matcher matcher = Pattern.compile("^\\d+$").matcher(next);
                    Customer customer;
                    if (matcher.find()) {
                        int id = new Integer(next).intValue();
                        customer = customerService.getCustomerById(id);
                    } else {
                        customer = customerService.getCustomerByOpenid(next);
                    }
                    if (customer != null) {
                        String wxOpenid = customer.getWxOpenid();
                        // 在微信模板里支持通配符自动替换: id/nickName/wxopenId
                        extra.put("id", "" + customer.getId());
                        extra.put("wxopenId", wxOpenid);
                        extra.put("nickName", customer.getNickName());
                        baseMessage.setTarget(wxOpenid);
                        messageService.putMessage(baseMessage);
                    }*/
                }
            }
        };
    }


    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
    }
}
