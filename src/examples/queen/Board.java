/* A N-Queen Solver
 * 
 * 11-FEB-2013	
 * Written by: Hao Wu
 * bugs report to: Haowu@cs.nuim.ie
 */
package examples.queen;
import java.util.ArrayList;
import java.util.List;
import atongmu.ast.Var;
import atongmu.ast.ConstDecl;
import atongmu.type.BoolType;
import atongmu.value.BoolValue;

public final class Board{
	private int row;
	private int col;
	private Var board[][];	

	public Board(int r, int c){
		row = r;
		col = c;
		if (row<=0 || col<=0) throw new QueenException("Error: the size of the board cannot be zero.");
		if(row!=col) throw new QueenException("Error: the board has to be square.");
		board = new Var[row][col];		
		initialise();
	}

	private void initialise(){
		int count=0;
		for (int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				board[i][j]=new Var("C"+count++,new BoolType());
				ConstDecl c=new ConstDecl(board[i][j]);
			}
		}
	}
	
	public List<Var> getDiagonal(int c, int r){
		List<Var> elements = new ArrayList<Var>();
		int i = c;
		int j = r;

		if (c>=row || c<0) throw new QueenException("Error: row index is out of bound.");
		if (r>=col || r<0) throw new QueenException("Error: col index is out of bound.");

		/* do it in 2 directions */
		for(i=c+1,j=r+1;i<row && j<col;i++,j++)	elements.add(board[i][j]);
		for(i=c-1,j=r-1;i>=0 && j>=0;i--,j--) elements.add(board[i][j]);
		for(i=c-1,j=r+1;i>=0 && j<col;i--,j++) elements.add(board[i][j]);
		for(i=c+1,j=r-1;i<row && j>=0;i++,j--) elements.add(board[i][j]);
		
		return elements;
	}

	public List<Var> getRow(int r){
		List<Var> elements = new ArrayList<Var>();					
		if (r>=row || r<0) throw new QueenException("Error: row index is out of bound.");
		for (int j=0;j<col;j++) elements.add(board[r][j]);
		return elements;
	}
	
	public List<Var> getCol(int c){
		List<Var> elements = new ArrayList<Var>();
		if (c<0 || c>=col) throw new QueenException("Error: column index is out of bound.");
		for (int j=0;j<row;j++) elements.add(board[j][c]);
		return elements;
	}

	public int row(){return row;}
	public int col(){return col;}
	public Var getCell(int i, int j){
		if (i>=row || i<0) throw new QueenException("Error: row index is out of bound.");
		if (j>=col || j<0) throw new QueenException("Error: col index is out of bound.");
		
		return board[i][j];
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<row;i++){
			for (int j=0;j<col;j++){
				if ( ((BoolValue) board[i][j].getValue()).getValue())
					sb.append("|Q| ");
				else
					sb.append("|X| ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
