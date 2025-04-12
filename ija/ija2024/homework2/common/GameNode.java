package ija.ija2024.homework2.common;

import java.util.Arrays;

public class GameNode {
    private Position position;
    private Side[] side;
    private NodeType type;
    public GameNode(int row, int col, NodeType type, Side... sides){
        side = new Side[sides.length];
        this.position = new Position(row, col);
        this.type = type;
        this.side = sides;
    }

    public GameNode(int row, int col, NodeType type){
        this.position = new Position(row, col);
        this.type = type;
        side = new Side[0];
    }

    public Position getPosition(){
        return this.position;
    }

    public boolean isBulb(){
        return this.type == NodeType.BULB;
    }

    public boolean isLink(){
        return this.type == NodeType.WIRE;
    }

    public boolean isPower(){
        return this.type == NodeType.SOURCE;
    }

    public boolean containsConnector(Side s){

        if (this.side == null) {
            return false;
        }
        return Arrays.asList(this.side).contains(s); 
    }

    public void turn(){
        int i = 0;
        for(Side side : this.side){
            int changeDirection = side.ordinal();
            this.side[i] = Side.values()[(changeDirection + 1) % Side.values().length];
            i++;
        }
    }
    public boolean light(){
        return true;
    }
}
