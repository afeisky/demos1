CREATE DEFINER=`chaofei_wu`@`%` TRIGGER `tclsysdb`.`bd_data_BEFORE_UPDATE` BEFORE UPDATE ON `bd_data` FOR EACH ROW
BEGIN

IF (NEW.parser_text <> OLD.parser_text) then 
 SET NEW.parser_history=CONCAT(NEW.parser_history,"\n#####@@@@@#####comment-->",date_format(now(),'%Y-%m-%d %H:%i:%s'),",",OLD.parser_mail,"\n",OLD.parser_text);
END IF;
END