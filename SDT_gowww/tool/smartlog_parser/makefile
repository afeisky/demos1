
objects =  main.o

sdt_smart1_parser : $(objects) 
	gcc -o ../sdt_smartlog_parser $(objects)

$(objects) : version.h 1_0_0.h 1_1_0.h 2_0_0.h
main.o : main.c

clean :
	rm ../sdt_smartlog_parser $(objects)
