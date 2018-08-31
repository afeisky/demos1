
objects =  main.o 

proinfo_parser : $(objects) 
	gcc -o ../sdt_proinfo_parser $(objects)

$(objects) : CFG_PRODUCT_INFO_File.h
main.o : main.c

clean :
	rm ../sdt_proinfo_parser $(objects)
