/*
Copyright (c) 2010 Geoff Lewis <gsl@gslsrc.net>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package net.gslsrc.dmex.settings;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A general container for multiple {@link Setting} instances.
 *
 * @author Geoff Lewis
 */
public class Settings implements Iterable<Setting>, Serializable {
    private static final long serialVersionUID = -4274002277566543004L;

    private String exid;

    private Map<String, Setting> map = new LinkedHashMap<String, Setting>();

    public Settings(String exid) {
        if (exid == null) {
            throw new NullPointerException("Exercise id is null");
        }

        this.exid = exid;
    }

    public final String getExerciseId() {
        return exid;
    }

    public Settings add(Setting setting) {
        if (setting != null) {
            map.put(setting.getId(), setting);
        }

        return this;
    }

    public Collection<Setting> getSettings() {
        return map.values();
    }

    @Override
    public Iterator<Setting> iterator() {
        return map.values().iterator();
    }

    public Setting getSetting(String sid) {
        return map.get(sid);
    }

    public <S extends Setting> S getSetting(Class<S> settingCls, String sid) {
        if (map.containsKey(sid)) {
            return settingCls.cast(map.get(sid));
        }

        return null;
    }
}
