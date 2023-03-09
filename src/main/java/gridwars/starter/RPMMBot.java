package gridwars.starter;

import cern.ais.gridwars.api.Coordinates;
import cern.ais.gridwars.api.UniverseView;
import cern.ais.gridwars.api.bot.PlayerBot;
import cern.ais.gridwars.api.command.MovementCommand;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class RPMMBot implements PlayerBot {

    private MovementCommand.Direction direction1;
    private MovementCommand.Direction direction2;
    private UniverseView universe;
    private Coordinates breeder;

    
    public RPMMBot() {

    }



    private int getCellPopulation(Coordinates cell) {
        return this.universe.getPopulation(cell);
    }

    private int abs(int n) {
        if (n < 0) {
            return -n;
        }
        return n;
    }

    private void calculateDirection(Coordinates bot, Coordinates enemy) {
        int x = enemy.getX() - bot.getX();
        int y = enemy.getY() - bot.getY();
        if (x > 0) {
            this.direction1 = MovementCommand.Direction.RIGHT;
        } else {
            this.direction1 = MovementCommand.Direction.LEFT;
        }
        if (y > 0) {
            this.direction2 = MovementCommand.Direction.DOWN;
        } else {
            this.direction2 = MovementCommand.Direction.UP;
        }
    }

    private void getDirection(UniverseView universeView, List<Coordinates> myCells) {
        for (int i = 0; i < universeView.getUniverseSize(); i++) {
            for (int j = 0; j < universeView.getUniverseSize(); j++) {
                if (!universeView.isEmpty(i, j)) {
                    if (!universeView.belongsToMe(i, j)) {
                        calculateDirection(myCells.get(0), universeView.getCoordinates(i, j));
                        return;
                    }
                }
            }
        }
    }

    private void makePlace(UniverseView universeView, List<MovementCommand> commandList, Coordinates cell, MovementCommand.Direction move) {
//        if (this.botCells.getOrDefault(cell, 0) < 100) {
//            return;
//        }
//        Coordinates neighbour = cell.getNeighbour(move);
//        if (universeView.belongsToMe(neighbour) && this.botCells.getOrDefault(neighbour, 0) == 100) {
//            makePlace(universeView, commandList, cell.getNeighbour(move), move);
//        }
//        int count = 0;
//        if (universeView.belongsToMe(neighbour)) {
//            count = this.botCells.getOrDefault(cell, 0)-this.botCells.getOrDefault(neighbour, 0);
//        } else {
//            this.botCells.getOrDefault(cell, 0);
//        }
//        if (count > 0) {
//            makeMove(universeView, commandList, cell, move, count);
//            this.botCells.replace(cell, this.botCells.getOrDefault(cell, 0) - count);
//        }
    }

    private void move(List<MovementCommand> commandList, Coordinates cell, MovementCommand.Direction move, int split) {
        if (split > getCellPopulation(cell)) {
            split = getCellPopulation(cell);
        }
        if (split == 0) {
            return;
        }
        commandList.add(new MovementCommand(cell, move, split));
    }
    
    private void makeMove(UniverseView universeView, List<MovementCommand> commandList, Coordinates cell, MovementCommand.Direction move, int split) {
        int neighbourPopulation = this.getCellPopulation(cell.getNeighbour(move));
        if (neighbourPopulation > 0) {
            if (neighbourPopulation + split > 100) {
                split = 100 - neighbourPopulation;
            }
        }
//        String[] logData = {cell.toString(), Integer.toString(getCellPopulation(cell)), Integer.toString(split)};
//        String templateString = MessageFormat.format("Cell: {0}\tPopulation: {1}\tSplit: {2}", logData);
//        universeView.log(templateString);
        move(commandList, cell, move, split);
//        if (neighbourPopulation <= 0) {
//            this.botCells.put(cell.getNeighbour(move), -universeView.getPopulation(cell.getNeighbour(move)));
//        }
//        if (this.botCells.getOrDefault(cell.getNeighbour(move), 0) != 0) {
//            if (split + this.botCells.getOrDefault(cell.getNeighbour(move), 0) > universeView.getMaximumPopulation()) {
//                split = universeView.getMaximumPopulation() - this.botCells.getOrDefault(cell.getNeighbour(move), 0);
//            }
//        }
    }

    public void getNextCommands(UniverseView universeView, List<MovementCommand> commandList) {
        this.universe = universeView;
        List<Coordinates> myCells = universeView.getMyCells();

//        if (universeView.getCurrentTurn() < 3) {
//            getDirection(universeView, myCells);
//            breeder = myCells.get(0);
//        }

        for (Coordinates cell : myCells) {
//            if (cell.getX() == breeder.getX() && cell.getY() == breeder.getY()) {
//                if (getCellPopulation(cell) >= 20) {
//                    if (direction1 == MovementCommand.Direction.LEFT) {
//                        makeMove(universeView, commandList, cell, MovementCommand.Direction.RIGHT, getCellPopulation(cell)-10);
//                        breeder = cell.getRight();
//                    } else {
//                        makeMove(universeView, commandList, cell, MovementCommand.Direction.LEFT, getCellPopulation(cell)-10);
//                        breeder = cell.getLeft();
//                    }
//                }
//                continue;
//            }
            int currentPopulation = this.getCellPopulation(cell);
//
//            if (currentPopulation > 12) {
//                int split = (currentPopulation-10)/3;
//                makeMove(universeView, commandList, cell, direction1, split);
//                makeMove(universeView, commandList, cell, direction2, split);
//            }
            int split = 1;
            for (MovementCommand.Direction direction : MovementCommand.Direction.values()) {
                if (universeView.getPopulation(cell.getNeighbour(direction)) < universeView.getPopulation(cell)) {
                    split++;
                }
                if (!universeView.belongsToMe(cell.getNeighbour(direction))) {
                    split++;
                }
            }
            if (currentPopulation > (split+10)) {
                for (MovementCommand.Direction direction : MovementCommand.Direction.values()) {
                    if (universeView.getPopulation(cell.getNeighbour(direction)) < universeView.getPopulation(cell)) {
                        commandList.add(new MovementCommand(cell, direction, (currentPopulation-10)/split));
                    }
                    if (!universeView.belongsToMe(cell.getNeighbour(direction))) {
                        commandList.add(new MovementCommand(cell, direction, (currentPopulation-10)/split));
                    }
                }
            }
        }
    }

    public void getNextCommandsv1(UniverseView universeView, List<MovementCommand> commandList) {
        this.universe = universeView;
        List<Coordinates> myCells = universeView.getMyCells();
//        this.botCells.clear();
//        for (Coordinates cell : myCells) {
//            this.botCells.put(cell, universeView.getPopulation(cell));
//        }


        if (universeView.getCurrentTurn() < 3) {
            getDirection(universeView, myCells);
            breeder = myCells.get(0);
        }

        for (Coordinates cell : myCells) {
            if (cell.getX() == breeder.getX() && cell.getY() == breeder.getY()) {
                if (getCellPopulation(cell) >= 20) {
                    if (direction1 == MovementCommand.Direction.LEFT) {
                        makeMove(universeView, commandList, cell, MovementCommand.Direction.RIGHT, getCellPopulation(cell)-10);
                        breeder = cell.getRight();
                    } else {
                        makeMove(universeView, commandList, cell, MovementCommand.Direction.LEFT, getCellPopulation(cell)-10);
                        breeder = cell.getLeft();
                    }
                }
                continue;
            }
            int currentPopulation = this.getCellPopulation(cell);

            if (currentPopulation > 12) {
                int split = (currentPopulation-10)/3;
                makeMove(universeView, commandList, cell, direction1, split);
                makeMove(universeView, commandList, cell, direction2, split);
//                universeView.log(Integer.toString(split));
//                switch (this.direction) {
//                    case UP:
////                        makePlace(universeView, commandList, cell.getLeft(), MovementCommand.Direction.LEFT);
//                        makeMove(universeView, commandList, cell, MovementCommand.Direction.LEFT, split);
////                        makePlace(universeView, commandList, cell.getUp(), MovementCommand.Direction.UP);
//                        makeMove(universeView, commandList, cell, MovementCommand.Direction.UP, split);
////                        makePlace(universeView, commandList, cell.getRight(), MovementCommand.Direction.RIGHT);
//                        makeMove(universeView, commandList, cell, MovementCommand.Direction.RIGHT, split);
//                        break;
//                    case DOWN:
////                        makePlace(universeView, commandList, cell.getLeft(), MovementCommand.Direction.LEFT);
//                        makeMove(universeView, commandList, cell, MovementCommand.Direction.LEFT, split);
////                        makePlace(universeView, commandList, cell.getDown(), MovementCommand.Direction.DOWN);
//                        makeMove(universeView, commandList, cell, MovementCommand.Direction.DOWN, split);
////                        makePlace(universeView, commandList, cell.getRight(), MovementCommand.Direction.RIGHT);
//                        makeMove(universeView, commandList, cell, MovementCommand.Direction.RIGHT, split);
//                        break;
//                    case LEFT:
////                        makePlace(universeView, commandList, cell.getLeft(), MovementCommand.Direction.LEFT);
//                        makeMove(universeView, commandList, cell, MovementCommand.Direction.LEFT, split);
////                        makePlace(universeView, commandList, cell.getUp(), MovementCommand.Direction.UP);
//                        makeMove(universeView, commandList, cell, MovementCommand.Direction.UP, split);
////                        makePlace(universeView, commandList, cell.getDown(), MovementCommand.Direction.DOWN);
//                        makeMove(universeView, commandList, cell, MovementCommand.Direction.DOWN, split);
//                        break;
//                    case RIGHT:
////                        makePlace(universeView, commandList, cell.getDown(), MovementCommand.Direction.DOWN);
//                        makeMove(universeView, commandList, cell, MovementCommand.Direction.DOWN, split);
////                        makePlace(universeView, commandList, cell.getUp(), MovementCommand.Direction.UP);
//                        makeMove(universeView, commandList, cell, MovementCommand.Direction.UP, split);
////                        makePlace(universeView, commandList, cell.getRight(), MovementCommand.Direction.RIGHT);
//                        makeMove(universeView, commandList, cell, MovementCommand.Direction.RIGHT, split);
//                        break;
//                }
            }
        }
//        universeView.log(commandList.toString());
//        universeView.log(this.board.toString());
    }
}
