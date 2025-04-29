-- Grant all privileges to root user from any host
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'Libin3137@mysql';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES; 