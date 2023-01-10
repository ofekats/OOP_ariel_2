# OOP_ariel_2
## Ex2_1 - part 1
### The purpose of the assignment
In this assignment-part 1: we created n number of files, each of them with random numbers of lines. 
we counted how many lines there are (in all of the files we created) in 3 ways:  
    1. one by one.  
    2. each file with different thread.  
    3. each file with different thread exceute by threadpool.  
    for each way we checked the time it took.  
### result
We got that the third way, of using the threadpool, was the fastest.  
After that, the second way - each thread for each file, and counting one by one took the longest.    
  
Threads allows a program to operate more efficiently by doing multiple things at the same time.  
So instead of the program to count file one by one, the program can generate  
threads which will count the lines of the files at the same time. But, when we use threads, the program create a new  
thread for each task (-for each file) and after the thread ending his task he dies. so instead we can use threadpool. That mean  
that the threadpool create a number of threads that when each thread ending his task he will take a new task  
to do until there is no tasks left. So as we can see the runtime of threadpool is better than threads which is better than doing tasks one by one.  
## Ex2_2 - part 2
### The purpose of the assignment
In this assignment-part 2: Create a new type that represents an asynchronous task with priority and a new ThreadPool type that supports tasks with priority.  
### sumup
we created Task objects that have a priority and a return value.  
we implemented Comparable so that Task objects can be compared (the lowest priority first).  
we created a new of ThreadPool that support those Task.  
we used adapter design pattern to adapt from FutureTask (Runnuble- without return value) to our Task (Callable).  
  
our CustomExecutor gets queue with our Tasks and execute them by their priority (the lowest priority first).  

