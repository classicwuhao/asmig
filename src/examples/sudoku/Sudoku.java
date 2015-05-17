/* A Sudoku Solver
 * 
 * 09-NOV-2012	
 * Written by: Hao Wu
 * bugs report to: Haowu@cs.nuim.ie
 */
package examples.sudoku;
import java.util.List;
import java.util.ArrayList;
import atongmu.ast.Var;
import atongmu.ast.ConstDecl;
import atongmu.type.IntType;

public class Sudoku{
	private  int _g_row;
	private  int _g_column;
	private final int GRID_ROW=9;
	private final int GRID_COLUMN=9;
	private final int BLOCK_ROW=3;
	private final int BLOCK_COLUMN=3;
	private final int BLOCK_LENGTH=(GRID_ROW*GRID_COLUMN)/(BLOCK_ROW*BLOCK_COLUMN);
	private int cellCount=0;
	private Block[] blocks;
	private List<Var> Cells = new ArrayList<Var>();

	public Sudoku(){
		CreateGrid(GRID_ROW,GRID_COLUMN);
		//this(9,9);
	}
	
	private Sudoku(final int row, final int column){
		CreateGrid(row,column);
	}

	private void CreateGrid(final int row, final int column){
		this._g_row = row;
		this._g_column = column;
		this.cellCount = row * column;
		
		/* Create constants */
		for (int i=0;i<cellCount;i++)
			Cells.add(CreateCell(i+1));
			
		/* fill in cells into the blocks */
		blocks = FillInBlocks();
		
	}
	
	private Block[] FillInBlocks(){
		int k=0;int r=0;
		int t=0;int u=0;
		
		Block blocks[] = new Block[BLOCK_LENGTH];
		for (int i=0;i<BLOCK_LENGTH;blocks[i++]=new Block());
		
		while (k<BLOCK_LENGTH){
			t=r;
			for (int i=0,c=0;i<BLOCK_LENGTH;i++,c++){
				if (c>=BLOCK_COLUMN) {c=0;r+=BLOCK_LENGTH-BLOCK_COLUMN;}
				blocks[k].addCell(Cells.get(r++));
			}
			if (++u>=BLOCK_ROW){
				u=0;
				r=t-(GRID_ROW/BLOCK_ROW-1)*BLOCK_COLUMN + GRID_COLUMN * BLOCK_ROW ;
			}
			else{
				r=t+BLOCK_COLUMN;
			}
			k++;
		}

		return blocks;
	}

	public Var getCell(int i, int j){
		if (i<=0 || i>GRID_ROW)
			throw new SudokuException("Error: invalid row number.");

		if (j<=0 || j>GRID_COLUMN)
			throw new SudokuException("Error: invalid column number.");
		
		return Cells.get((i-1)*GRID_ROW+(j-1));
	}

	private Var CreateCell(int label){
		Var v =  (label> 0 && label<10) ?
				new Var("C0"+label,new IntType()) :
				new Var("C"+label,new IntType());
		ConstDecl d = new ConstDecl(v);
		return v;
	}
	
	public int CellCount(){return cellCount;}
	public int getRow(){return _g_row;}
	public int getColumn(){return _g_column;}
	public List<Var> getAllCells(){return Cells;}
	public int getBlockLength(){return BLOCK_LENGTH;}
	public Var[] getBlock(int k){
		if (k<=0 || k>BLOCK_LENGTH)
			throw new SudokuException("Error: no such block exists.");

		return blocks[k-1].getCells();
	}
	public Var[] getGridRowCells(int r){
		if (r<=0 || r>GRID_ROW)
			throw new SudokuException("Error: invalid row number.");

		Var[] vars = new Var[GRID_COLUMN];
		
		for (int i=0,d=(r-1)*GRID_ROW,k=0;i<GRID_COLUMN;i++)
			vars[k++]=Cells.get(d++);

		return vars;
	}
	public Var[] getGridColumnCells(int c){
		if (c<=0 || c>GRID_COLUMN)
			throw new SudokuException("Error: invalid row number.");

		Var[] vars = new Var[GRID_ROW];
		
		for (int i=0,d=c-1,k=0;i<GRID_ROW;i++,d+=GRID_ROW)
			vars[k++]=Cells.get(d);

		return vars;
	}
	public String toString(){
		StringBuffer str = new StringBuffer();

		for (int i=0;i<cellCount;i++){
			str.append(Cells.get(i).getValue()+" | ");
			if ( (i+1) % this._g_column==0)
				str.append("\n-----------------------------------------------------\n");
		}

		return str.toString();
	}

	private class Block {
		private Var bCells[];
		private int total=0;
		private int _b_row=0;
		private int _b_column=0;		
		private int index=0;		

		public Block(){this(BLOCK_ROW,BLOCK_COLUMN);}
		private Block(final int row, final int column){
			_b_row = row;
			_b_column = column;
			total = row * column;
			bCells = new Var[total];
		}

		public void addCell(Var v){
			if (index>=total) throw new SudokuException("Error: block index is out of bound.");
			bCells[index++]=v;
		}

		public Var[] getCells(){return bCells;}
		
		public String toString(){
			StringBuffer str = new StringBuffer();		
			for (int i=0;i<total;i++){
				str.append(bCells[i]+" | ");
				if ((i+1)%_b_column==0){
					str.append("\n--------------------------------------\n");				
				}
			}
			return str.toString();
		}

	}

	
}
