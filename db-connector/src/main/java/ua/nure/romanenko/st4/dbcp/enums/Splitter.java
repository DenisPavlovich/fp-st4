package ua.nure.romanenko.st4.dbcp.enums;

public enum Splitter {
    AND {
        @Override
        public String toString() {
            return " AND ";
        }
    }, OR {
        @Override
        public String toString() {
            return " OR ";
        }
    }
}