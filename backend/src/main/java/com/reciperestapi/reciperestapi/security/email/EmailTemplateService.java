package com.reciperestapi.reciperestapi.security.email;

import jakarta.inject.Inject;
import jakarta.mail.MessagingException;

public class EmailTemplateService {
    @Inject
    private EmailService emailService;

    public void sendAuthenticationEmailToClient(String to, String userName, String confirmationUrl, String otp) throws MessagingException {
        String text =
                         "<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\n" +
                         "    <tbody><tr>\n" +
                         "        <td align=\"center\" style=\"padding: 20px;\">\n" +
                         "            <table role=\"presentation\" width=\"100%\" style=\"max-width: 600px; background-color: #ffffff; border-radius: 10px; border: 1px solid #cccccc;\">\n" +
                         "                <tbody><tr>\n" +
                         "                    <td style=\"padding: 20px;\">\n" +
                         "                        <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\n" +
                         "                            <tbody><tr>\n" +
                         "                                <td style=\"text-align: center;\">\n" +
                         "                                    <a href=\"http://localhost:3000/\" style=\"display: inline-block; text-decoration: none; color: #232828; font-weight: 800; font-size: 1.5em; padding: 10px 10px 10px 35px;\">Dish Discovery</a>\n" +
                         "                                </td>\n" +
                         "                            </tr>\n" +
                         "                            <tr>\n" +
                         "                                <td style=\"padding: 20px 0;\">\n" +
                         "                                    <h2 style=\"font-size: 1.5em; font-weight: bold; color: #232828;\">One Try Password (OTP)</h2>\n" +
                         "                                </td>\n" +
                         "                            </tr>\n" +
                         "                            <tr>\n" +
                         "                                <td style=\"padding: 10px 0;\">\n" +
                         "                                    <p style=\"font-size: 0.9em; color: #717171;\">To authenticate, please use the following One Time Password (OTP):</p>\n" +
                         "                                </td>\n" +
                         "                            </tr>\n" +
                         "                            <tr>\n" +
                         "                                <td style=\"text-align: center; padding: 10px;\">\n" +
                         "                                    <div style=\"display: inline-block;padding: 10px;border-right: 2px solid #ccc;border-left: 2px solid #ccc;border-radius: 10px;\">\n" +
                         "                                        <h2 style=\"font-size: 1.5em;\">" + otp + "</h2>\n" +
                         "                                    </div>\n" +
                         "                                </td>\n" +
                         "                            </tr>\n" +
                         "                            <tr>\n" +
                         "                                <td style=\"padding: 10px 0;\">\n" +
                         "                                    <p style=\"font-size: 0.9rem;color: #717171;margin: 10px 0;\">This code is valid for 5 minutes.</p>\n" +
                         "                                </td>\n" +
                         "                            </tr>\n" +
                         "                            <tr>\n" +
                         "                                <td style=\"padding: 10px 0;border-bottom: 1px solid #cccccc;\">\n" +
                         "                                    <p style=\"font-size: 0.9em; color: #717171;\">Don't share this OTP with anyone. Our customer service team will never ask you for your password, OTP, credit card or banking info.</p>\n" +
                         "                                    <p style=\"font-size: 0.9em;color: #717171;margin: 10px 0;\">Yours sincerely, Dish Discovery</p>\n" +
                         "                                </td>\n" +
                         "                            </tr>\n" +
                         "                            <tr>\n" +
                         "                                <td style=\"text-align: center;padding: 30px 0;border-top: 1px solid #cccccc;\">\n" +
                         "                                    <p style=\"font-size: 0.9em;\">\n" +
                         "                                        <a href=\"#\" style=\"text-decoration: none; color: #717171;\">Help</a>\n" +
                         "                                        <span style=\"padding: 0 10px;\">|</span>\n" +
                         "                                        <a href=\"#\" style=\"text-decoration: none; color: #717171;\">Security</a>\n" +
                         "                                        <span style=\"padding: 0 10px;\">|</span>\n" +
                         "                                        <a href=\"#\" style=\"text-decoration: none; color: #717171;\">Contact</a>\n" +
                         "                                    </p>\n" +
                         "                                </td>\n" +
                         "                            </tr>\n" +
                         "                            </tbody>" +
                         "                      </table>\n" +
                         "                    </td>\n" +
                         "                </tr>\n" +
                         "                </tbody>" +
                         "          </table>\n" +
                         "        </td>\n" +
                         "    </tr>\n" +
                         "    </tbody>\n" +
                         "</table>";
        emailService.sendOtpEmail(to, "Account activation.", text);
    }
    
}
