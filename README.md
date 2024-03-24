This program contains an interesting way to find Pi, called Monte Carlo. It uses a fixed thread pool of the size of the CPU cores in the system to generate points. 
It also protects against race conditions on updates to the shared global variable by using a ReentrantLock.  
