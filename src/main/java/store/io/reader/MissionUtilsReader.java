package store.io.reader;

import camp.nextstep.edu.missionutils.Console;

public class MissionUtilsReader implements Reader {
    public String readLine() {
        try {
            return Console.readLine();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
