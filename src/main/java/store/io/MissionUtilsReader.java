package store.io;

import camp.nextstep.edu.missionutils.Console;

public class MissionUtilsReader {
    public String readLine() {
        try {
            return Console.readLine();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
