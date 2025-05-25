SHOW EVENTS;
SET GLOBAL event_scheduler = ON;

/* Delete all expired tokens */
CREATE EVENT IF NOT EXISTS delete_old_tokens
ON SCHEDULE EVERY 1 HOUR
DO
DELETE t1
FROM token t1
JOIN (
    SELECT token_id 
    FROM token 
    WHERE token_expires < current_timestamp()
) t2 ON t1.token_id = t2.token_id;

/* delete all expired otp codes */
CREATE EVENT IF NOT EXISTS delete_old_otp
ON SCHEDULE EVERY 1 HOUR
DO
DELETE o1
FROM otp o1
JOIN (
    SELECT otp_id 
    FROM otp 
    WHERE expiresAt < current_timestamp()
) o2 ON o1.otp_id = o2.otp_id;

SELECT * FROM cookingdb.token;
