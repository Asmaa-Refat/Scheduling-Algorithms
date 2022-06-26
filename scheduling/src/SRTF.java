//shortest-remaining-time-first program that solves the starvation problem through
// preventing any more processes to enter until all the processes are finished

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Process
{
    public String name;
    public String color;
    public int arrivalTime;
    public int burstTime;
    public int priorityNumber;
    public int quantumTime;
    public int waitingTime;
    public int turnAroundTime;

    public Process() {}

    public Process(String name, String color, int arrivalTime, int burstTime, int priorityNumber, int quantumTime)
    {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
        this.quantumTime = quantumTime;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public void setArrivalTime(int arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }

    public void setBurstTime(int burstTime)
    {
        this.burstTime = burstTime;
    }

    public void setQuantumTime(int quantumTime)
    {
        this.quantumTime = quantumTime;
    }

    public void setWaitingTime(int waitingTime) {this.waitingTime = waitingTime;}

    public void setTurnAroundTime(int turnAroundTime) {this.turnAroundTime = turnAroundTime;}

    public void setPriorityNumber(int priorityNumber)
    {
        this.priorityNumber = priorityNumber;
    }

    public String getName()
    {
        return name;
    }

    public String getColor()
    {
        return color;
    }

    public int getArrivalTime()
    {
        return arrivalTime;
    }

    public int getBurstTime()
    {
        return burstTime;
    }

    public int getPriorityNumber()
    {
        return priorityNumber;
    }

    public int getQuantumTime()
    {
        return quantumTime;
    }

    public int getTurnAroundTime() { return turnAroundTime; }

    public int getWaitingTime() { return waitingTime; }
}

public class SRTF
{
    static void calculateWaitingTime(ArrayList<Process> processes, int size, int contextSwitching)
    {
        int timer = 0, minimum = 900000, smallestIndex = -1;
        int flag = 0;
        int burstTime[] = new int[size];
        int temp = -1;
        ArrayList<String> processOrder = new ArrayList<String>();
        ArrayList<String> processExecution = new ArrayList<String>();
        for(int i = 0; i < size; i++)
        {
            burstTime[i] = processes.get(i).burstTime;
        }

        int finished = size;
        while (finished != 0)
        {
            int j = 0;
            while(processes.get(j).arrivalTime <= timer)
            {
                flag = 1;
                if((burstTime[j] < minimum) && (burstTime[j] > 0))
                {
                    minimum = burstTime[j];
                    smallestIndex = j;
                }
                j++;
                if(j == size)
                    break;
            }
            if (flag == 0)         //inner while loop wasn't entered
            {
                timer++;
                continue;
            }

            if(temp != smallestIndex)     //getting the order of the processes
            {
                temp = smallestIndex;
                processes.get(temp).waitingTime += contextSwitching;
                processOrder.add(processes.get(temp).name);
            }

            burstTime[smallestIndex] =  burstTime[smallestIndex] - 1;
            minimum = burstTime[smallestIndex];


            if (minimum <= 0)
                minimum = 900000;

            int k = 0;
            while(processes.get(k).arrivalTime <= timer)       //calculate the waiting time
            {
                if((k != smallestIndex) && (burstTime[k] > 0) )
                    processes.get(k).waitingTime++;

                k++;
                if(k == size)
                    break;
            }

            //solving the starvation problem through putting a threshold, when the waiting time of any process
            // exceeds it, we put it in to get processed.
            for(int i = 0; i < size; i++)
            {
                if(processes.get(i).waitingTime >= 50)
                {
                    burstTime[i] = 0;
                    smallestIndex = i;
                }
            }

            if(burstTime[smallestIndex] == 0)
            {
                  processExecution.add(processes.get(smallestIndex).name);
                  finished--;
            }
            timer++;
        }
        System.out.print("Processes execution order :");
        System.out.print(processOrder);
        System.out.println();
        System.out.print("Processes execution order 2:");
        System.out.print(processExecution);
        System.out.println();
    }
    static void calculateTurnAroundTime(ArrayList<Process> processes, int size)
    {
        for (int i = 0; i < size; i++)
            processes.get(i).turnAroundTime = processes.get(i).burstTime + processes.get(i).waitingTime;
    }

    static void calculateAverageTime(ArrayList<Process> processes, int size, int contextSwitching)
    {
           double sumWaitingTime = 0;
           double sumTurnAroundTime = 0;
           calculateWaitingTime(processes, size, contextSwitching);
           calculateTurnAroundTime(processes, size);
           System.out.println("SRTF :-");
           System.out.println("-----");
           System.out.println("Process    Waiting Time    TurnAround Time");

           for(int i = 0; i < size; i++)
           {
               sumWaitingTime += processes.get(i).waitingTime;
               sumTurnAroundTime += processes.get(i).turnAroundTime;
               System.out.println("P" + (i+1) + "         " + processes.get(i).waitingTime + "               " + processes.get(i).turnAroundTime);
           }

           System.out.println("Average waiting time = " + (double)sumWaitingTime / size);
           System.out.println("Average turnAround time = " + (double)sumTurnAroundTime / size);
    }

    public static void main(String[] args)
    {
        ArrayList<Process> processes = new ArrayList<Process>();
        Process p1 = new Process("p1","",0,8,4,4);
        Process p2 = new Process("p2","",1,4,9,3);
        Process p3 = new Process("p3","",2,9,3,5);
        Process p4 = new Process("p4","",3,5,10,2);
        processes.add(p1);
        processes.add(p2);
        processes.add(p3);
        processes.add(p4);

        Scanner input = new Scanner(System.in);

        System.out.println("Enter context switching");
        int contextSwitching = input.nextInt();
        int size = processes.size();
        calculateAverageTime(processes, size, contextSwitching);
    }
}
