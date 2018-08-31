
objects =  main.o filename_parser.o

sdt_filename_parser : $(objects) 
	gcc -o ../sdt_filename_parser $(objects)

$(objects) : filename_parser.h
main.o : main.c
filename_parser.o : filename_parser.c 
clean :
	rm ../sdt_filename_parser $(objects)
