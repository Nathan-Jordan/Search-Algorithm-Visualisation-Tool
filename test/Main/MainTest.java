package Main;

import Algorithms.DepthFirstSearch;
import Algorithms.SearchAlgorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void changeAlgorithm() {
        Main main = new Main();
        main.init();

        SearchAlgorithm searchAlgorithm = main.searchAlgorithm;
        main.changeAlgorithm(new DepthFirstSearch());
        assertNotEquals(searchAlgorithm, main.searchAlgorithm);
    }

    @Test
    void init() {
        Main main = new Main();
        main.init();

        assertNotNull(main.searchAlgorithm);
    }
}