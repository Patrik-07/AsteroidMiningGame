package Game;

import Resources.Resource;

import java.util.ArrayList;

public class ResourceList {
    private ArrayList<Resource> list;

    /**
     * A ResourceList paraméter nélküli konstruktora.
     */
    public ResourceList() {
        list = new ArrayList<>();
    }

    /**
     * A ResourceList copy konstruktora.
     * @param resourceList Másolni kívánt lista.
     */
    public ResourceList(ResourceList resourceList) {
        list = new ArrayList<>(resourceList.list);
    }

    /**
     * Hozzáad egy resource-t és egy hozzátartozó darabszámot a listához.
     * @param resource Hozzáadni kívánt nyersanyag.
     */
    public void addResource(Resource resource) {
        list.add(resource);
    }

    /**
     * Megmondja, hogy egy nyersanyag benne van-e listában.
     * @param resource Keresni kívánt nyersanyag.
     * @return Igazzal tér vissza, ha a nyersanyag benne van-e a listában. Egyébként hamissal.
     */
    public boolean isContain(Resource resource) {
        for (Resource r : list)
            if (resource.getClass() == r.getClass())
                return true;
        return false;
    }

    /**
     * Megmondja, hogy a paraméterként kapott listával kompatibilis-e az adott lista.
     * @param resourceList Összehasonlíta kívánt lista.
     * @return Igazzal tér vissza, ha azonos típusú és darabszámú nyersanyag van az adott listában,
     * mint a paraméterként kapott. Egyébként hamissal.
     */
    public boolean isCompatible(ResourceList resourceList) {
        if (list.size() >= resourceList.list.size()) {
            ArrayList<Resource> temp = new ArrayList<>(resourceList.list);
            for (Resource r1 : list)
                temp.removeIf(r2 -> r1.getClass() == r2.getClass());
            return temp.size() == 0;
        } else return false;
    }

    /**
     * Kivonja a paraméterként kapott listát az adott listából.
     * @param resourceList Kivonni kívánt lista.
     */
    public void subtraction(ResourceList resourceList) {
        ArrayList<Resource> temp = new ArrayList<>(list);
        for (Resource r1 : resourceList.list)
            for (Resource r2 : temp)
                if (r1.getClass() == r2.getClass()) {
                    temp.remove(r2);
                    break;
                }
        list = temp;
    }

    /**
     * Megmondja, hogy összesen mennyi nyersanyag van a listában.
     * @return Listában lévő összes nyersanyag darabszáma.
     */
    public int resourceCount() {
        return list.size();
    }

    public ArrayList<Resource> getList() {
        return list;
    }
}