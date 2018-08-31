CREATE DEFINER=`chaofei_wu`@`%` TRIGGER `tclsysdb`.`bd_data_BEFORE_INSERT` BEFORE INSERT ON `bd_data` FOR EACH ROW
BEGIN
  SET NEW.parser_history=CONCAT("\n#####@@@@@#####comment-->",date_format(now(),'%Y-%m-%d %H:%i:%s'),",",NEW.parser_mail,"\n",NEW.parser_text);
END