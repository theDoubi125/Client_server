package argo.jdom;

import java.util.ArrayList;
import java.util.Iterator;

final class JsonArray_NodeList extends ArrayList
{
    final Iterable field_74730_a;

    JsonArray_NodeList(Iterable par1Iterable)
    {
        this.field_74730_a = par1Iterable;
        Iterator var2 = this.field_74730_a.iterator();

        while (var2.hasNext())
        {
            JsonNode var3 = (JsonNode)var2.next();
            this.add(var3);
        }
    }
}
