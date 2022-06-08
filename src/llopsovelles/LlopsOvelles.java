package llopsovelles;

public class LlopsOvelles {

    static final int NUM_OVELLES = 2;
    static final int NUM_LLOPS = 2;

    public static void main(String[] args) throws InterruptedException {
        MonitorRiu monitor = new MonitorRiu();
        Thread[] threads = new Thread[NUM_OVELLES + NUM_LLOPS];
        int x = 0;
        for (int i = 0; i < NUM_OVELLES; i++) {
            threads[x] = new Thread(new Ovella(i, monitor));
            threads[x].start();
            x++;
        }
        for (int i = 0; i < NUM_LLOPS; i++) {
            threads[x] = new Thread(new Llop(i, monitor));
            threads[x].start();
            x++;
        }
        for (int i = 0; i < NUM_OVELLES + NUM_LLOPS; i++) {
            threads[i].join();
        }
        System.out.println("ACABA LA SIMULACIÓ");
    }
}

class Llop implements Runnable {
    int id;
    MonitorRiu monitor;

    public Llop(int id, MonitorRiu monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                monitor.entrarLlop(id);
                System.out.println("\tLlop " +id+ " beu");
                Thread.sleep(5000);
                monitor.sortirLlop(id);
            } catch (InterruptedException ex) {
            }
        }
    }
}

class Ovella implements Runnable {
    int id;
    MonitorRiu monitor;

    public Ovella(int id, MonitorRiu monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                monitor.entrarOvella(id);
                System.out.println("Ovella " +id+ " beu");
                Thread.sleep(500);
                monitor.sortirOvella(id);
            } catch (InterruptedException ex) {
            }
        }
    }
}

class MonitorRiu {
    volatile private int numOvelles = 0;

    synchronized void entrarLlop(int id) {
        System.out.println("\tEntra Llop " + id);
        if (numOvelles == 1) {
            System.out.println("El llop " + id+ " ha menjat una ovella!!!!!!");
        }
    }

    synchronized void sortirLlop(int id) {
        System.out.println("\tSurt llop " + id);
    }

    synchronized void entrarOvella(int id) {
        try {
            this.notifyAll();
            if (numOvelles == 0) {
                this.wait();
            }
            this.notifyAll();
            numOvelles++;
        } catch (InterruptedException ex) {
        }
        System.out.println("Entra Ovella " + id);
    }

    synchronized void sortirOvella(int id) {
        try{
            this.notifyAll();
            if (numOvelles == 2){
                this.wait();
            }
            numOvelles--;
        }catch (InterruptedException ex){
        }
        System.out.println("Surt Ovella " + id);
    }
}
