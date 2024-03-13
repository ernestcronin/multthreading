This is a multi-threaded application to load users and retrieve them. 
Controller V1 uses SpringBoot's @async annotation to create threads and returns
CompletableFutures. Very convenient!
Controller V2 instantiates CompletableFutures. Good practice for understanding how
the future works behind the scenes with functions like runAsync(), supplyAsync(),
thenApply(), and thenAccept().

Load users:
There is a file MOCK_DATA.csv. 
Postman - POST request, in Body select form-data. 
key = files, value = path/of/file