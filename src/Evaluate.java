
public class Evaluate {
	
	static int midGame = 1750;			// Mid Game Material Value
	
	// Taken from http://chessprogramming.wikispaces.com/Simplified+evaluation+function
	static int pawnBoard[][]={
        { 0,  0,  0,  0,  0,  0,  0,  0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {10, 10, 20, 30, 30, 20, 10, 10},
        { 5,  5, 10, 25, 25, 10,  5,  5},
        { 0,  0,  0, 20, 20,  0,  0,  0},
        { 5, -5,-10,  0,  0,-10, -5,  5},
        { 5, 10, 10,-20,-20, 10, 10,  5},
        { 0,  0,  0,  0,  0,  0,  0,  0}};
    static int rookBoard[][]={
        { 0,  0,  0,  0,  0,  0,  0,  0},
        { 5, 10, 10, 10, 10, 10, 10,  5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        { 0,  0,  0,  5,  5,  0,  0,  0}};
    static int knightBoard[][]={
        {-50,-40,-30,-30,-30,-30,-40,-50},
        {-40,-20,  0,  0,  0,  0,-20,-40},
        {-30,  0, 10, 15, 15, 10,  0,-30},
        {-30,  5, 15, 20, 20, 15,  5,-30},
        {-30,  0, 15, 20, 20, 15,  0,-30},
        {-30,  5, 10, 15, 15, 10,  5,-30},
        {-40,-20,  0,  5,  5,  0,-20,-40},
        {-50,-40,-30,-30,-30,-30,-40,-50}};
    static int bishopBoard[][]={
        {-20,-10,-10,-10,-10,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5, 10, 10,  5,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 10, 10, 10, 10,  0,-10},
        {-10, 10, 10, 10, 10, 10, 10,-10},
        {-10,  5,  0,  0,  0,  0,  5,-10},
        {-20,-10,-10,-10,-10,-10,-10,-20}};
    static int queenBoard[][]={
        {-20,-10,-10, -5, -5,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0,-10},
        { -5,  0,  5,  5,  5,  5,  0, -5},
        {  0,  0,  5,  5,  5,  5,  0, -5},
        {-10,  5,  5,  5,  5,  5,  0,-10},
        {-10,  0,  5,  0,  0,  0,  0,-10},
        {-20,-10,-10, -5, -5,-10,-10,-20}};
    static int kingMidBoard[][]={
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-20,-30,-30,-40,-40,-30,-30,-20},
        {-10,-20,-20,-20,-20,-20,-20,-10},
        { 20, 20,  0,  0,  0,  0, 20, 20},
        { 20, 30, 10,  0,  0, 10, 30, 20}};
    static int kingEndBoard[][]={
        {-50,-40,-30,-20,-20,-30,-40,-50},
        {-30,-20,-10,  0,  0,-10,-20,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-30,  0,  0,  0,  0,-30,-30},
        {-50,-30,-30,-30,-30,-30,-30,-50}};
    
    public static boolean MATERIAL = false;			// Material Evaluation Flag
    public static boolean ATTACK = false;			// Attack Evaluation Flag
    public static boolean MOVABILITY = false;		// Movability Evaluation Flag
    public static boolean POSITIONAL = false;		// Positional Evaluation Flag
    public static int pawnValue = 100;				// Pawn Value
    public static int rookValue = 500;				// Rook Value
    public static int knightValue = 300;			// Knight Value
    public static int bishopValue = 300;			// Bishop Value
    public static int queenValue = 900;				// Queen Value
    
    /**
	 * Evaluation Function
	 */
	public static int evaluate(int listLength, int depth) {
	
		int value = 0;
		value += evaluateAll(listLength, depth);
		MoveGenerator.flipBoard();
		value -= evaluateAll(listLength, depth);
		MoveGenerator.flipBoard();
	
		return -(value + depth*50);
	}
	
	/**
	 * Evaluate Attack, Material, Movability, Positions
	 */
	public static int evaluateAll(int listLength, int depth) {
		
		int value = 0;					// Total Value
	    int material = 0;				// Material Value
	    int attack = 0;					// Attack Value
	    int movability = 0;				// Movability Value
	    int positional = 0;				// Positional Value

		String temp = MoveGenerator.kingAPos;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {

				if(MoveGenerator.chessBoard[i][j].equals("P")) {
					if(MATERIAL) material+=pawnValue;
					if(POSITIONAL) positional+=pawnBoard[i][j];
					MoveGenerator.kingAPos = i+""+j;
					if(ATTACK) if(!MoveGenerator.isKingSafe(0, 0, 0, 0, MoveGenerator.chessBoard[0][0])) attack -= pawnValue/2;
				}
				else if(MoveGenerator.chessBoard[i][j].equals("R")) {
					if(MATERIAL) material+=rookValue;
					if(POSITIONAL) positional+=rookBoard[i][j];
					MoveGenerator.kingAPos = i+""+j;
					if(ATTACK) if(!MoveGenerator.isKingSafe(0, 0, 0, 0, MoveGenerator.chessBoard[0][0])) attack -= rookValue;
				}
				else if(MoveGenerator.chessBoard[i][j].equals("K")) {
					if(MATERIAL) material+=knightValue;
					if(POSITIONAL) positional+=knightBoard[i][j];
					MoveGenerator.kingAPos = i+""+j;
					if(ATTACK) if(!MoveGenerator.isKingSafe(0, 0, 0, 0, MoveGenerator.chessBoard[0][0])) attack -= knightValue;
				}
				else if(MoveGenerator.chessBoard[i][j].equals("B")) {
					if(MATERIAL) material+=bishopValue;
					if(POSITIONAL) positional+=bishopBoard[i][j];
					MoveGenerator.kingAPos = i+""+j;
					if(ATTACK) if(!MoveGenerator.isKingSafe(0, 0, 0, 0, MoveGenerator.chessBoard[0][0])) attack -= bishopValue;
				}
				else if(MoveGenerator.chessBoard[i][j].equals("Q")) {
					if(MATERIAL) material+=queenValue;
					if(POSITIONAL) positional+=queenBoard[i][j];
					MoveGenerator.kingAPos = i+""+j;
					if(ATTACK) if(!MoveGenerator.isKingSafe(0, 0, 0, 0, MoveGenerator.chessBoard[0][0])) attack -= queenValue;
				}
			}
		}

		MoveGenerator.kingAPos = temp;
		if(ATTACK) if(!MoveGenerator.isKingSafe(0, 0, 0, 0, MoveGenerator.chessBoard[0][0])) attack -= 200;
		attack = attack/2;
		
		if(POSITIONAL) {
			// Get the Kings Position
			int u = MoveGenerator.kingAPos.charAt(0) - '0';
			int v = MoveGenerator.kingAPos.charAt(1) - '0';

			if(material >= midGame) {
				positional += kingMidBoard[u][v];
			}
			else {
				positional += kingEndBoard[u][v];
			}
		}

		if(MOVABILITY) {
			movability += listLength;
			if(listLength == 0) { // Current side is in CheckMate or StaleMate
				if(!MoveGenerator.isKingSafe(0, 0, 0, 0, MoveGenerator.chessBoard[0][0])) { // CheckMate
					movability += -200000*depth;
				}
				else { // StaleMate
					movability += -150000*depth;
				}
			}
		}

		if(MATERIAL) value += material;
		if(ATTACK) value += attack;
		if(MOVABILITY) value += movability;
		if(POSITIONAL) value += positional;

		return value;
	}
	
	/**
	 * Evaluate Attack
	 */
	public static int evaluateAttack() {
		
		String temp = MoveGenerator.kingAPos;
		int value = 0;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {

				if(MoveGenerator.chessBoard[i][j].equals("P")) {
					MoveGenerator.kingAPos = i+""+j;
					if(!MoveGenerator.isKingSafe(0, 0, 0, 0, MoveGenerator.chessBoard[0][0])) value -= 64;
				}
				else if(MoveGenerator.chessBoard[i][j].equals("R")) {
					MoveGenerator.kingAPos = i+""+j;
					if(!MoveGenerator.isKingSafe(0, 0, 0, 0, MoveGenerator.chessBoard[0][0])) value -= 500;
				}
				else if(MoveGenerator.chessBoard[i][j].equals("K")) {
					MoveGenerator.kingAPos = i+""+j;
					if(!MoveGenerator.isKingSafe(0, 0, 0, 0, MoveGenerator.chessBoard[0][0])) value -= 300;
				}
				else if(MoveGenerator.chessBoard[i][j].equals("B")) {
					MoveGenerator.kingAPos = i+""+j;
					if(!MoveGenerator.isKingSafe(0, 0, 0, 0, MoveGenerator.chessBoard[0][0])) value -= 300;
				}
				else if(MoveGenerator.chessBoard[i][j].equals("Q")) {
					MoveGenerator.kingAPos = i+""+j;
					if(!MoveGenerator.isKingSafe(0, 0, 0, 0, MoveGenerator.chessBoard[0][0])) value -= 900;
				}
			}
		}

		MoveGenerator.kingAPos = temp;
		if(!MoveGenerator.isKingSafe(0, 0, 0, 0, MoveGenerator.chessBoard[0][0])) value -= 200;
		return value/2;
	}
	
	/**
	 * Evaluate Material
	 */
	public static int evaluateMaterial() {

		int value = 0;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {

				if(MoveGenerator.chessBoard[i][j].equals("P")) {
					value += 100;
				}
				else if(MoveGenerator.chessBoard[i][j].equals("R")) {
					value += 500;
				}
				else if(MoveGenerator.chessBoard[i][j].equals("K")) {
					value += 300;
				}
				else if(MoveGenerator.chessBoard[i][j].equals("B")) {
					value += 300;
				}
				else if(MoveGenerator.chessBoard[i][j].equals("Q")) {
					value += 900;
				}
			}
		}
		return value;
	}
	
	/**
	 * Evaluate Movability
	 */
	public static int evaluateMovability(int listLength, int depth) {
		
		int value = 0;
		value += listLength;
		if(listLength == 0) { // Current side is in CheckMate or StaleMate
			if(!MoveGenerator.isKingSafe(0, 0, 0, 0, MoveGenerator.chessBoard[0][0])) { // CheckMate
				value += -200000*depth;
			}
			else { // StaleMate
				value += -150000*depth;
			}
		}
		return value;
	}
	
	/**
	 * Evaluate Positions
	 */
	public static int evaluatePositional(int material) {
		
		int value = 0;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {

				if(MoveGenerator.chessBoard[i][j].equals("P")) {
					value += pawnBoard[i][j];
				}
				else if(MoveGenerator.chessBoard[i][j].equals("R")) {
					value += rookBoard[i][j];
				}
				else if(MoveGenerator.chessBoard[i][j].equals("K")) {
					value += knightBoard[i][j];
				}
				else if(MoveGenerator.chessBoard[i][j].equals("B")) {
					value += bishopBoard[i][j];
				}
				else if(MoveGenerator.chessBoard[i][j].equals("Q")) {
					value += queenBoard[i][j];
				}
				else if(MoveGenerator.chessBoard[i][j].equals("A")) {
					if(material >= midGame) {
						value += kingMidBoard[i][j];
					}
					else {
						value += kingEndBoard[i][j];
					}
				}
			}
		}

		return value;
	}
}
