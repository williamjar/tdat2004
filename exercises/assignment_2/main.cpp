#include <iostream>
#include <list>
#include <thread>
#include <vector>
#include <condition_variable>
#include <mutex>

using namespace std;

class Workers
{

private:
    vector<thread> threads;
    list<function<void()>> tasks;
    mutex wait_mutex;
    condition_variable cv;
    int noOfThreads;
    bool stopThread = false;

public:
    explicit Workers(int threads)
    {
        noOfThreads = threads;
    }

    void post(const function<void()> &task)
    {
        {
            lock_guard<mutex> lock(wait_mutex);
            tasks.emplace_back(task);
        }

        cv.notify_one();
    }

    void start()
    {
        for (int i = 0; i < noOfThreads; i++)
        {
            threads.emplace_back([this] {
                while (true)
                {
                    function<void()> task;
                    {

                    
                        unique_lock<mutex> lock(wait_mutex);

                        while (!stopThread && tasks.empty())
                        {
                            cv.wait(lock);
                        }

                        if (tasks.empty())
                        {
                            return;
                        }

                        task = *tasks.begin();
                        tasks.pop_front();
                    }

                    task_timeout(25);
                    task();
                }
            });
        }
    }

    void join()
    {
        for (auto &thread : threads)
            thread.join();
        stop();
    } 

    void stop()
    {
        stopThread = true;
        cv.notify_all();
    }

    void task_timeout(int timeoutMs)
    {
        this_thread::sleep_for(std::chrono::milliseconds(timeoutMs));
    }
};

int main()
{
    Workers worker_threads(4);
    Workers event_loop(1);
    worker_threads.start(); 
    event_loop.start();     

    worker_threads.post([] {
       
        cout << "Task A running" << endl;
    });
    worker_threads.post([] {
        cout << "Task B running" << endl;
    });
    event_loop.post([] {
        cout << "Event loop: task C running" << endl;
    });
    event_loop.post([] {
        cout << "Hello from task D, Event loop" << endl;

    });

    this_thread::sleep_for(5s);
    worker_threads.stop();
    event_loop.stop();

    worker_threads.join();
    event_loop.join();
}