package com.boots.service;

import javax.jms.JMSException;

public interface INotifier {
    void notify(String email, String message) throws JMSException;
}
