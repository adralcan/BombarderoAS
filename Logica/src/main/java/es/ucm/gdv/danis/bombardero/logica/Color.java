package es.ucm.gdv.danis.bombardero.logica;

public class Color {
    //TODO: ADAPTAR TODO ESTO
    enum color {
        BLACK (0),
        GREEN(1),
        RED(2),
        PURPLE(3),
        DARKGREEN(4),
        ORANGE(5),
        DARKBLUE(6),
        YELLOW(7),
        PINK(8),
        WHITE(9),
        BLUEGREEN(10),
        LIGHTBLUE(11),
        KHAKI(12),
        LIGHTORANGE(13),
        BLUE(14),
        LIGHTGREEN(15);

        private int value;
        color(int val){value = val;}
        public int getValue(){
            return value;
        }

    }
}
