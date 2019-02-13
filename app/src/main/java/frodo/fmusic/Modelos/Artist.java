package frodo.fmusic.Modelos;

public class Artist extends Base {

    public Artist(String name, long id) {
        super(id, name, 0, 0);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int valor) {
        count+=valor;
    }
}

