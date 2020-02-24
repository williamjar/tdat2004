#include <iostream>
#include <list>
#include <thread>
#include <vector>
#include <condition_variable>
#include <mutex>


using namespace std;

class Workers{

private:
    vector<thread> threads;
    list<function<void()>> tasks;
    mutex wait_mutex;
    condition_variable cv;
    int noOfThreads;
    bool stopThread = false;

public:

    explicit Workers(int threads) {
        noOfThreads = threads;
    }


    void post(const function<void()> &task){
        {
            lock_guard<mutex> lock(wait_mutex);
            tasks.emplace_back(task);
        }

        cv.notify_one();
    }

    void start(){
        for(int i=0; i<noOfThreads; i++){
                threads.emplace_back([this]{
                    while (true)
                    {
                        function<void()> task;
                        {

                            unique_lock<mutex> lock(wait_mutex);

                            while(!stopThread && tasks.empty()){
                                cv.wait(lock);
                            }

                            if (tasks.empty())
                            {
                                return;
                            }

                            task = *tasks.begin(); // Copy task for later use
                            tasks.pop_front(); // Remove task from list
                        }

                        task_timeout(25);
                        task(); // Run task outside of mutex lock

                    }

                });
            }
    }

    void join(){
        for (auto &thread : threads)
            thread.join();
        stop();
    }

    void stop(){
        stopThread = true;
        cv.notify_all();
    }

    void task_timeout(int timeoutMs){
        this_thread::sleep_for(std::chrono::milliseconds(timeoutMs));
    }

};

int main() {
    Workers worker_threads(4);
    Workers event_loop(1);
    worker_threads.start(); // Create 4 internal threads
    event_loop.start(); // Create 1 internal thread

    worker_threads.post([] {
        // Task A
        cout << "hello from task a" << endl;
    });
    worker_threads.post([] {
        // Task B
        // Might run in parallel with task A
        cout << "hello from task b" << endl;
    });
    event_loop.post([] {
        // Task C
        // Might run in parallel with task A and B
        cout << "hello from task C, Event loop" << endl;
    });
    event_loop.post([] {

        cout << "Hello from task D, Event loop" << endl;
        // Task D
        // Will run after task C
        // Might run in parallel with task A and B
    });



    this_thread::sleep_for(5s);
    worker_threads.stop();
    event_loop.stop();

    worker_threads.join();
    event_loop.join();



}