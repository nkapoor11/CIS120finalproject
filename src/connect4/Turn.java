package connect4;

public class Turn {
    private int num;
    private int row;
	private int col;
	
	public Turn() {
	    
	}
	
	public Turn(int row, int column) {
		this.row = row;
		col = column;
	}
	
	public Turn(int number) {
		num = number;
	}
	
	public Turn(int row, int column, int number) {
		this.row = row;
		col = column;
		num = number;
	}
	
	public int getCol() {
        return col;
    }
	
	public void setRow(int row) {
        this.row = row;
    }
    
    public void setCol(int column) {
        col = column;
    }
	
	public int getRow() {
		return row;
	}

	public void setNum(int number) {
        num = number;
    }
	
	public int getNum() {
		return num;
	}
}