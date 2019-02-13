package frodo.fmusic.Modelos;

import java.net.PortUnreachableException;

public abstract class Base {

    protected long id;
    protected String name;
    protected int count;
    protected int duration;

    public Base(long id, String name, int count,int duration) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.duration = duration;
    }

    public abstract long getId();

    public abstract void setId(long id);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract int getCount();

    public abstract void setCount(int count);

    public abstract int getDuration();

    public abstract void setDuration(int duration);
}
