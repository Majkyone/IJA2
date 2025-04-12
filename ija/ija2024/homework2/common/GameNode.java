package ija.ija2024.homework2.common;

import java.util.Arrays;
public class GameNode{
    private Position position;
    private Side[] side;
    private NodeType type;
    private boolean powered = false;
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
        return this.powered;
    }

    public NodeType getType(){
        return this.type;
    }

    public void setPowered(boolean powered){
        this.powered = powered;
    }

    public Side[] getSides(){
        return this.side;
    }

    public boolean isPowered(){
        return this.powered;
    }

    public String toString(){
        return String.format("{%s[%d@%d][%s]}", type.swichSideToString(), position.getRow(), position.getCol(), String.join(" ", Arrays.toString(side).replaceAll("[\\[\\] ]", "")));
    }
}
