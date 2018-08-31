CREATE DEFINER=`chaofei_wu`@`%` TRIGGER `tclsysdb`.`bd_json_AFTER_INSERT` AFTER INSERT ON `bd_json` FOR EACH ROW
BEGIN
call bd_json_tr(1,NEW.filename);
END