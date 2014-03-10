package net.sinclairstudios.android.dumplings.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DumplingOrderList implements Serializable, Iterable<DumplingOrder> {

    private final List<DumplingOrder> dumplingOrders;

    public DumplingOrderList(List<DumplingOrder> dumplingOrders) {
        this.dumplingOrders = dumplingOrders;
    }

    @Override
    public Iterator<DumplingOrder> iterator() {
        return dumplingOrders.iterator();
    }

    public Iterable<DumplingOrderViewHook> getDumplingOrderViewHooks() {
        List<DumplingOrderViewHook> orderViewHookList = new ArrayList<DumplingOrderViewHook>();
        for (DumplingOrder dumplingOrder : dumplingOrders) {
            orderViewHookList.add(new DumplingOrderViewHook(dumplingOrder));
        }
        return orderViewHookList;
    }
}
