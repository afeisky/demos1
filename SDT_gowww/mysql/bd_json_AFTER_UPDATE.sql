CREATE DEFINER=`chaofei_wu`@`%` TRIGGER `tclsysdb`.`bd_json_AFTER_UPDATE` AFTER UPDATE ON `bd_json` FOR EACH ROW
BEGIN
call bd_json_tr(1,NEW.filename);
END