package solutions.problem10;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Solution to problem ten, part two of Advent of Code.
 * https://adventofcode.com/2023/day/10
 *
 * Answer is
 */
public class Problem10Part2 {
    private char[][] lines;
    private final ArrayList<int[]> pathHistory = new ArrayList<>();
    private final ArrayList<int[]> allEnclosedCoords = new ArrayList<>();
    private final ArrayList<int[]> rejectedCoords = new ArrayList<>();

    private class Pipe {
        private char pipeType;
        private int[] curCoords;
        private int[] prevCoords;

        public Pipe(char pipeType, int[] coords) {
            this.pipeType = pipeType;
            this.curCoords = coords;
        }

        public Pipe(char pipeType, int x, int y) {
            this.pipeType = pipeType;
            this.curCoords = new int[] { x, y };
        }

        public void setCurCoords(int[] curCoords) {
            this.prevCoords = this.curCoords;
            this.curCoords = curCoords;
            this.pipeType = lines[curCoords[0]][curCoords[1]];
        }

        public char getPipeType() {
            return pipeType;
        }

        public int[] getCurCoords() {
            return curCoords;
        }

        public int[] getPrevCoords() {
            return prevCoords;
        }
    }

    /** Constructor */
    public Problem10Part2() {
        try {
            File input = new File("resources/Problem10Input.txt");
            Scanner scanner = new Scanner(input);

            // Read all the lines
            ArrayList<char[]> linesOccurences = new ArrayList<>();
            while (scanner.hasNextLine()) {
                char[] line = scanner.nextLine().toCharArray();
                linesOccurences.add(line);
                System.out.println(line);
            }
            lines = linesOccurences.toArray(new char[0][0]);
            System.out.println();

            findFurthestPoint();
            findEnclosedAreas();

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void findFurthestPoint() {
        // Find the location of S
        Pipe traverse = null;
        for (int x=0; x < lines.length; x++) {
            for (int y=0; y < lines[0].length; y++) {
                if (lines[x][y] == 'S') {
                    traverse = new Pipe(lines[x][y], x, y);
                }
            }
        }
        assert traverse != null;
        pathHistory.add(traverse.getCurCoords());

        // Find all the pipe locations
        do {
            findNextPipe(traverse);
            pathHistory.add(traverse.getCurCoords());
        } while (traverse.getPipeType() != 'S');

        System.out.println();

        // Find the areas enclosed by the pipes
        // Find values one apart from each other on a single axis? Or one on both?
    }

    private void findNextPipe(Pipe pipe1) {
        // Given the pipe type, find the connection pipes
        int[] northCoords = new int[] { pipe1.getCurCoords()[0]-1, pipe1.getCurCoords()[1] };
        Pipe north = null;
        if ((northCoords[0] >= 0) && (northCoords[1] >= 0) &&
            (northCoords[0] < lines.length) && (northCoords[1] < lines[0].length)) {
            north = new Pipe(lines[northCoords[0]][northCoords[1]], northCoords);
        }

        int[] southCoords = new int[] { pipe1.getCurCoords()[0]+1, pipe1.getCurCoords()[1] };
        Pipe south = null;
        if ((southCoords[0] >= 0) && (southCoords[1] >= 0) &&
            (southCoords[0] < lines.length) && (southCoords[1] < lines[0].length)) {
            south = new Pipe(lines[southCoords[0]][southCoords[1]], southCoords);
        }

        int[] eastCoords = new int[] { pipe1.getCurCoords()[0], pipe1.getCurCoords()[1]+1 };
        Pipe east = null;
        if ((eastCoords[0] >= 0) && (eastCoords[1] >= 0) &&
            (eastCoords[0] < lines.length) && (eastCoords[1] < lines[0].length)) {
            east = new Pipe(lines[eastCoords[0]][eastCoords[1]], eastCoords);
        }

        int[] westCoords = new int[] { pipe1.getCurCoords()[0], pipe1.getCurCoords()[1]-1 };
        Pipe west = null;
        if ((westCoords[0] >= 0) && (westCoords[1] >= 0) &&
            (westCoords[0] < lines.length) && (westCoords[1] < lines[0].length)) {
            west = new Pipe(lines[westCoords[0]][westCoords[1]], westCoords);
        }

        switch (pipe1.getPipeType()) {
            case '|':
                if ((south != null) && ((north == null) ||
                        ((pipe1.getPrevCoords()[0] == north.getCurCoords()[0]) &&
                         (pipe1.getPrevCoords()[1] == north.getCurCoords()[1])))) {
                    pipe1.setCurCoords(south.getCurCoords());
                } else if (north != null) {
                    pipe1.setCurCoords(north.getCurCoords());
                }
                break;

            case '-':
                if ((west != null) && ((east == null) ||
                    ((pipe1.getPrevCoords()[0] == east.getCurCoords()[0]) &&
                     (pipe1.getPrevCoords()[1] == east.getCurCoords()[1])))) {
                    pipe1.setCurCoords(west.getCurCoords());
                } else if (east != null) {
                    pipe1.setCurCoords(east.getCurCoords());
                }
                break;

            case 'L':
                if ((north != null) && ((east == null) ||
                    ((pipe1.getPrevCoords()[0] == east.getCurCoords()[0]) &&
                     (pipe1.getPrevCoords()[1] == east.getCurCoords()[1])))) {
                    pipe1.setCurCoords(north.getCurCoords());
                } else if (east != null) {
                    pipe1.setCurCoords(east.getCurCoords());
                }
                break;

            case 'J':
                if ((north != null) && ((west == null) ||
                    ((pipe1.getPrevCoords()[0] == west.getCurCoords()[0]) &&
                     (pipe1.getPrevCoords()[1] == west.getCurCoords()[1])))) {
                    pipe1.setCurCoords(north.getCurCoords());
                } else if (west != null) {
                    pipe1.setCurCoords(west.getCurCoords());
                }
                break;

            case '7':
                if ((south != null) && ((west == null) ||
                    ((pipe1.getPrevCoords()[0] == west.getCurCoords()[0]) &&
                     (pipe1.getPrevCoords()[1] == west.getCurCoords()[1])))) {
                    pipe1.setCurCoords(south.getCurCoords());
                } else if (west != null) {
                    pipe1.setCurCoords(west.getCurCoords());
                }
                break;

            case 'F':
                if ((south != null) && ((east == null) ||
                    ((pipe1.getPrevCoords()[0] == east.getCurCoords()[0]) &&
                     (pipe1.getPrevCoords()[1] == east.getCurCoords()[1])))) {
                    pipe1.setCurCoords(south.getCurCoords());
                } else if (east != null) {
                    pipe1.setCurCoords(east.getCurCoords());
                }
                break;

            case 'S':
                if ((west != null) &&
                        ((west.getPipeType() == 'F') ||
                         (west.getPipeType() == '-') ||
                         (west.getPipeType() == 'L'))) {
                    pipe1.setCurCoords(west.getCurCoords());
                }
                else if ((east != null) &&
                        ((east.getPipeType() == '7') ||
                         (east.getPipeType() == '-') ||
                         (east.getPipeType() == 'J'))) {
                    pipe1.setCurCoords(east.getCurCoords());
                }
                else if ((north != null) &&
                        ((north.getPipeType() == '7') ||
                         (north.getPipeType() == '|') ||
                         (north.getPipeType() == 'F'))) {
                    pipe1.setCurCoords(north.getCurCoords());
                }
                else if (south != null) {
                    pipe1.setCurCoords(south.getCurCoords());
                }
                break;

            default:
                break;
        }
    }

    private void findEnclosedAreas() {
        for (int i = 1; i < pathHistory.size()-1; i++) {
            int[] adjacentSpace = isAdjacent(pathHistory.get(i-1), pathHistory.get(i), pathHistory.get(i+1));

            if (adjacentSpace.length > 0) {
                findEnclosedSpace(adjacentSpace);
            }
        }

        // Sort the coordinates by their x value, lowest to highest
        allEnclosedCoords.sort((Comparator<? super int[]>) (coords1, coords2) -> {
            // Compare y
            if (coords1[0] > coords2[0]) {
                return 1;
            } else if (coords1[0] < coords2[0]) {
                return -1;
            }

            // Compare x if y are the same
            if (coords1[1] > coords2[1]) {
                return 1;
            } else if (coords1[1] < coords2[1]) {
                return -1;
            }

            return 0;
        });

        System.out.println("\nCoordinates of enclosed spaces:");
        for (var coords : allEnclosedCoords) {
            System.out.println(Arrays.toString(coords));
        }
        System.out.println("\nTOTAL: " + allEnclosedCoords.size());
    }

    /**
     * Check to see if there's an enclosed space (or potential one)
     * @param prevLoc the location in the path you just came from
     * @param curLoc the current location in the path
     * @param nextLoc the location in the path you'll move to next
     * @return the first enclosed space location
     */
    private int[] isAdjacent(int[] prevLoc, int[] curLoc, int[] nextLoc) {
        // Make sure it isn't the previous or next location in the pipe
        boolean isPrevLoc;
        boolean isNextLoc;

        // Locations
        int[] northLoc = new int[] { curLoc[0]-1, curLoc[1] };
        int[] southLoc = new int[] { curLoc[0]+1, curLoc[1] };
        int[] eastLoc = new int[] { curLoc[0], curLoc[1]+1 };
        int[] westLoc = new int[] { curLoc[0], curLoc[1]-1 };

        // North side
        isPrevLoc = ((northLoc[0] == prevLoc[0]) && (northLoc[1] == prevLoc[1]));
        isNextLoc = ((northLoc[0] == nextLoc[0]) && (northLoc[1] == nextLoc[1]));
        boolean northAdj = !isPrevLoc && !isNextLoc;

        // South side
        isPrevLoc = ((southLoc[0] == prevLoc[0]) && (southLoc[1] == prevLoc[1]));
        isNextLoc = ((southLoc[0] == nextLoc[0]) && (southLoc[1] == nextLoc[1]));
        boolean southAdj = !isPrevLoc && !isNextLoc;

        // South side
        isPrevLoc = ((eastLoc[0] == prevLoc[0]) && (eastLoc[1] == prevLoc[1]));
        isNextLoc = ((eastLoc[0] == nextLoc[0]) && (eastLoc[1] == nextLoc[1]));
        boolean eastAdj = !isPrevLoc && !isNextLoc;

        // South side
        isPrevLoc = ((westLoc[0] == prevLoc[0]) && (westLoc[1] == prevLoc[1]));
        isNextLoc = ((westLoc[0] == nextLoc[0]) && (westLoc[1] == nextLoc[1]));
        boolean westAdj = !isPrevLoc && !isNextLoc;

        if (northAdj) {
            //System.out.println("North Adj: " + Arrays.toString(curLoc));
            //System.out.println("[" + northLoc[0] + ", " + northLoc[1] + "]");
            //System.out.println("--");
            return northLoc;
        } else if (southAdj) {
            //System.out.println("South Adj: " + Arrays.toString(curLoc));
            //System.out.println("[" + southLoc[0] + ", " + southLoc[1] + "]");
            //System.out.println("--");
            return southLoc;
        } else if (eastAdj) {
            //System.out.println("East Adj: " + Arrays.toString(curLoc));
            //System.out.println("[" + eastLoc[0] + ", " + eastLoc[1] + "]");
            //System.out.println("--");
            return eastLoc;
        } else if (westAdj) {
            //System.out.println("West Adj: " + Arrays.toString(curLoc));
            //System.out.println("[" + westLoc[0] + ", " + westLoc[1] + "]");
            //System.out.println("--");
            return westLoc;
        }
        return new int[0];
    }

    private void findEnclosedSpace(int[] firstLoc) {
        if (hasCoord(allEnclosedCoords, firstLoc) ||
                (firstLoc[0] < 0) ||
                (firstLoc[1] < 0) ||
                (firstLoc[0] > lines.length-1) ||
                (firstLoc[1] > lines[0].length-1) ||
                (lines[firstLoc[0]][firstLoc[1]] == '|') ||
                (lines[firstLoc[0]][firstLoc[1]] == '-')) {
            return;
        }

        // Create an arraylist of coords for the enclosed space
        ArrayList<int[]> enclosedCoords = new ArrayList<>();
        enclosedCoords.add(firstLoc);
        //System.out.println(Arrays.toString(firstLoc));

        // Find all coords in the enclosed space
        for (int i=0; i < enclosedCoords.size(); i++) {
            // Get the locations in each direction
            int[] curLoc = enclosedCoords.get(i);
            int[] north = new int[] { curLoc[0] - 1, curLoc[1] };
            int[] south = new int[] { curLoc[0] + 1, curLoc[1] };
            int[] east  = new int[] { curLoc[0], curLoc[1] + 1 };
            int[] west  = new int[] { curLoc[0], curLoc[1] - 1 };

            if (!hasCoord(pathHistory, north) &&
                !hasCoord(enclosedCoords, north) &&
                (curLoc[0]-1 > 0)) {
                //System.out.printf("%-12s | %s%n", "North", Arrays.toString(north));
                enclosedCoords.add(north);
            }
            if (!hasCoord(pathHistory, south) &&
                !hasCoord(enclosedCoords, south) &&
                (curLoc[0]+1 < lines.length)) {
                //System.out.printf("%-12s | %s%n", "South", Arrays.toString(south));
                enclosedCoords.add(south);
            }
            if (!hasCoord(pathHistory, east) &&
                !hasCoord(enclosedCoords, east) &&
                (curLoc[1]+1 < lines[0].length)) {
                //System.out.printf("%-12s | %s%n", "East", Arrays.toString(east));
                enclosedCoords.add(east);
            }
            if (!hasCoord(pathHistory, west) &&
                !hasCoord(enclosedCoords, west) &&
                (curLoc[1]-1 > 0)) {
                //System.out.printf("%-12s | %s%n", "West", Arrays.toString(west));
                enclosedCoords.add(west);
            }
        }

        if (preventEdges(enclosedCoords) && preventSpillage(enclosedCoords)) {
            allEnclosedCoords.addAll(enclosedCoords);
        } else {
            rejectedCoords.addAll(enclosedCoords);
        }
        //System.out.println();
    }

    /**
     * Make sure nothing reaches an edge
     * @param enclosedCoords
     * @return
     */
    private boolean preventEdges(ArrayList<int[]> enclosedCoords) {
        System.out.println();
        for (var coord : enclosedCoords) {
            System.out.println(Arrays.toString(coord));
            if ((coord[0] <= 0) ||
                (coord[1] <= 0) ||
                (coord[0] >= lines.length-1) ||
                (coord[1] >= lines[0].length-1) ||
                hasCoord(pathHistory, coord) ||
                hasCoord(allEnclosedCoords, coord)) {
                System.out.println("Invalid location found! Removing this location set!");
                return false;
            }
        }
        System.out.println("No invalid locations found!");
        return true;
    }

    /**
     * Check to make sure the enclosed space can't "leak"
     * @param enclosedCoords
     * @return
     */
    private boolean preventSpillage(ArrayList<int[]> enclosedCoords) {
        // Find the coords at the bottom of the enclosed space
        ArrayList<int[]> highestCoords = new ArrayList<>();
        for (var coords : enclosedCoords) {
            // If it's higher, remove previous ones and add these
            if (!highestCoords.isEmpty() && (highestCoords.get(0)[0] < coords[0])) {
                highestCoords.clear();
                highestCoords.add(coords);
            }
            // If it's the same, add it
            else if (!highestCoords.isEmpty() && highestCoords.get(0)[0] == coords[0]) {
                highestCoords.add(coords);
            }
            // If it's empty, just add
            else if (highestCoords.isEmpty()) {
                highestCoords.add(coords);
            }
        }

        // Sort the coordinates by their x value, lowest to highest
        highestCoords.sort((Comparator<? super int[]>) (coords1, coords2) -> {
            if (coords1[1] > coords2[1]) {
                return 1;
            } else if (coords1[1] < coords2[1]) {
                return -1;
            }
            return 0;
        });

        //System.out.println("Sorted coords");
        //for (var coords : highestCoords) {
        //    System.out.println(Arrays.toString(coords));
        //}
        //System.out.println("End sorted coords\n");

        // Check those coords to make sure there's no set of values where they could spill out
        for (int i=0; i < highestCoords.size(); i++) {
            char pipe0 = 'o';
            char pipe1 = 'o';
            char pipe2 = 'o';

            if (highestCoords.get(i)[0]+1 < lines.length) {
                if (highestCoords.get(i)[1]-1 < lines[0].length) {
                    pipe0 = lines[highestCoords.get(i)[0]+1][highestCoords.get(i)[1]-1];

                    // Check for leaking edge

                }
                if (highestCoords.get(i)[1] < lines[0].length) {
                    pipe1 = lines[highestCoords.get(i)[0]+1][highestCoords.get(i)[1]];
                }
                if (highestCoords.get(i)[1]+1 < lines[0].length) {
                    pipe2 = lines[highestCoords.get(i)[0]+1][highestCoords.get(i)[1]+1];
                }
            }

            if ((pipe0 == '7' && pipe1 == 'F') ||
                (pipe1 == '7' && pipe2 == 'F') ||

                (pipe0 == '7' && pipe1 == '|') ||
                (pipe1 == '7' && pipe2 == '|') ||

                (pipe0 == '7' && pipe1 == 'L') ||
                (pipe1 == '7' && pipe2 == 'L') ||

                (pipe0 == '|' && pipe1 == 'F') ||
                (pipe1 == '|' && pipe2 == 'F') ||

                (pipe0 == 'J' && pipe2 == 'F') ||
                (pipe1 == 'J' && pipe2 == 'F')) {
                return false;
            }
        }
        return true;
    }

    private boolean hasCoord(ArrayList<int[]> listOfCoords, int[] incomingCoords) {
        for (int[] coords : listOfCoords) {
            if ((incomingCoords[0] == coords[0]) && (incomingCoords[1] == coords[1])) {
                return true;
            }
        }
        return false;
    }
}