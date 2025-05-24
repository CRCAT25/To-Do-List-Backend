package net.thanhdevjava.to_do_list.service;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
}
