import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class UserInterface extends JPanel implements MouseListener, MouseMotionListener{

	static int squareSize = 64;									// Square Size
	int mouseX = 0;												// Mouse X
	int mouseY = 0;												// Mouse Y
	int newMouseX = 0;											// New Mouse X
	int newMouseY = 0;											// New Mouse Y
	boolean flag = true;										// Flag to move

	Image wp = new ImageIcon("images/wp.png").getImage();		// White Pawn
	Image wr = new ImageIcon("images/wr.png").getImage();		// White Rook
	Image wk = new ImageIcon("images/wk.png").getImage();		// White Knight
	Image wb = new ImageIcon("images/wb.png").getImage();		// White Bishop
	Image wq = new ImageIcon("images/wq.png").getImage();		// White Queen
	Image wa = new ImageIcon("images/wa.png").getImage();		// White King

	Image bp = new ImageIcon("images/bp.png").getImage();		// Black Pawn
	Image br = new ImageIcon("images/br.png").getImage();		// Black Rook
	Image bk = new ImageIcon("images/bk.png").getImage();		// Black Knight
	Image bb = new ImageIcon("images/bb.png").getImage();		// Black Bishop
	Image bq = new ImageIcon("images/bq.png").getImage();		// Black Queen
	Image ba = new ImageIcon("images/ba.png").getImage();		// Black King
	
	public UserInterface() {
		
		// Add a Mouse Listener
		this.addMouseListener(this);
		
		// Add a Mouse Motion Listener
		this.addMouseMotionListener(this);
		
		// Set Layout
		setLayout(new BorderLayout());
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		// Draw the Squares
		drawBoard(g);
		
		// Draw Icons
		drawIcons(g);
	}
	
	/**
	 * Draw the Chess Icons
	 */
	public void drawIcons(Graphics g) {
		
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				
				// Black Pieces
				if(MoveGenerator.chessBoard[i][j].equals("r")) {
					g.drawImage(br, 64*j, i*64, this);
				}
				if(MoveGenerator.chessBoard[i][j].equals("k")) {
					g.drawImage(bk, 64*j, i*64, this);
				}
				if(MoveGenerator.chessBoard[i][j].equals("b")) {
					g.drawImage(bb, 64*j, i*64, this);
				}
				if(MoveGenerator.chessBoard[i][j].equals("q")) {
					g.drawImage(bq, 64*j, i*64, this);
				}
				if(MoveGenerator.chessBoard[i][j].equals("a")) {
					g.drawImage(ba, 64*j, i*64, this);
				}
				if(MoveGenerator.chessBoard[i][j].equals("p")) {
					g.drawImage(bp, 64*j, i*64, this);
				}
				
				// White Pieces
				if(MoveGenerator.chessBoard[i][j].equals("R")) {
					g.drawImage(wr, 64*j, i*64, this);
				}
				if(MoveGenerator.chessBoard[i][j].equals("K")) {
					g.drawImage(wk, 64*j, i*64, this);
				}
				if(MoveGenerator.chessBoard[i][j].equals("B")) {
					g.drawImage(wb, 64*j, i*64, this);
				}
				if(MoveGenerator.chessBoard[i][j].equals("Q")) {
					g.drawImage(wq, 64*j, i*64, this);
				}
				if(MoveGenerator.chessBoard[i][j].equals("A")) {
					g.drawImage(wa, 64*j, i*64, this);
				}
				if(MoveGenerator.chessBoard[i][j].equals("P")) {
					g.drawImage(wp, 64*j, i*64, this);
				}
				
			}
		}
	}

	/**
	 * Draw the Chess Board
	 */
	public void drawBoard(Graphics g) {
		
		for(int i=0; i<8; i++) {
			// Even row
			if(i%2 == 0) {
				for(int j=0; j<8; j+=2) {
					g.setColor(Color.GRAY);
					g.fillRect(j*squareSize, i*squareSize, squareSize, squareSize);
					g.setColor(Color.WHITE);
					g.fillRect((j+1)*squareSize, i*squareSize, squareSize, squareSize);
				}
			}
			// Odd row
			else {
				for(int j=0; j<8; j+=2) {
					g.setColor(Color.WHITE);
					g.fillRect(j*squareSize, i*squareSize, squareSize, squareSize);
					g.setColor(Color.GRAY);
					g.fillRect((j+1)*squareSize, i*squareSize, squareSize, squareSize);
				}
			}
		}
		
		if(!flag) {
			// Highlight the piece
			g.setColor(Color.YELLOW);
			g.fillRect(mouseY*squareSize, mouseX*squareSize, squareSize, squareSize);
			
			// Highlight the possible moves
			String list = MoveGenerator.getPossibleMoves();
			String[] moves = list.split(";");
			int length = moves.length;
			for(int i=0; i<length; i++) {
				if(!moves[i].isEmpty() && moves[i].substring(0, 2).equals(mouseX+""+mouseY)) {
					g.fillRect((moves[i].charAt(3)-'0')*squareSize, (moves[i].charAt(2)-'0')*squareSize, squareSize, squareSize);
				}
				else if(moves[i].isEmpty() && length == 1) {
					System.out.println("CheckMate - No More Possible Moves !");
				}
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getX() < 8*squareSize && e.getY() < 8*squareSize) {
			
			if(flag) {
				mouseX = e.getY()/squareSize;
				mouseY = e.getX()/squareSize;
				repaint();
				flag = false;
			}
			else {
				newMouseX = e.getY()/squareSize;
				newMouseY = e.getX()/squareSize;
				repaint();
				flag = true;

				// Move the piece if allowed
				String move = mouseX+""+mouseY+""+newMouseX+""+newMouseY+MoveGenerator.chessBoard[newMouseX][newMouseY];
				if(MoveGenerator.isMoveAllowed(move)) {
					
					// Make the move
					MoveGenerator.makeMove(move);
					System.out.println("YOUR MOVE:");
					MoveGenerator.printBoard();
					
					// Computer's Move
					System.out.println("WAITING FOR COMPUTER TO MOVE...");
					MoveGenerator.flipBoard();
					long start = System.currentTimeMillis();
					String str = MoveGenerator.alphaBeta(MoveGenerator.globalDepth, 100000, -100000, "", 0);
					long end = System.currentTimeMillis();
					MoveGenerator.makeMove(str);
					MoveGenerator.flipBoard();
					System.out.println("TIME FOR COMPUTER'S MOVE: " + (end-start)/1000.0 + " SECONDS");
					System.out.println("COMPUTER'S MOVE:");
					MoveGenerator.printBoard();
					repaint();
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
