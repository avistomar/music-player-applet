import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.*;
import java.io.*;

//<Applet code="PlayerApplet" width=250 height=50> </Applet>

public class PlayerApplet extends Applet implements ActionListener
{
	private Player player;
	private FileDialog fd;
	private File file;
	private String str;
	private Frame f;
	private Button play,stop,open,about;
	
	public void init()
	{
		f = findParentFrame();
		
		// Designing Music player
		setLayout(new BorderLayout());
		play=new Button("Play");
		stop=new Button("Stop");
		open=new Button("Open");
		about=new Button("About");
		add(play,BorderLayout.WEST);
		add(stop,BorderLayout.EAST);
		add(open,BorderLayout.CENTER);
		add(about,BorderLayout.SOUTH);
		
		// Listening play,pause buttons
		play.addActionListener(this);
		stop.addActionListener(this);
		open.addActionListener(this);
		about.addActionListener(this);
	}
	
	//Handling play,pause buttons
	public void actionPerformed(ActionEvent ae) 
	{
		str = ae.getActionCommand();
		
		if( str == "Play" )
			playSound();
		else if( str == "Stop" )
			stopSound();
		else if ( str == "Open" )
			openFile();
		else if ( str == "About" ) {
			showStatus("_ _Encoded by @v!n@$#_ _");
		}
	}
	
	public void start()
	{
		setVisible(true);
	}
	
	public void stop()
	{
		setVisible(false);
	}
	
	//Parentframe for DialogBox
	private Frame findParentFrame() {
		Component c = getParent();
		while ( true ) {
			if ( c instanceof Frame)
				return (Frame)c;
			c = c.getParent();
		}
	}
	
	private void openFile()
	{
		if ( f != null ) {
			stopSound();
			fd=new FileDialog(f,"Select a mp3 file");
			fd.setVisible(true);
			file=new File(fd.getDirectory(),fd.getFile());
			playSound();
		}
	}
	
	private synchronized void playSound() 
	{
		if ( file == null ) {
			showStatus("No file selected");
			return;
		}
			
		try
			{
				player=Manager.createPlayer( (file.toURI()).toURL() );
				player.addControllerListener(new PlayerHandler());
				player.start();
				showStatus("Current Track:"+file);
			}
			catch(Exception e) 
			{
				System.out.println(e);
			}
	}
	
	private void stopSound()
	{
		if ( player == null )
			return;
		
		player.stop();
		Component control=player.getControlPanelComponent();
		
		if(control!=null)
			remove(control);
			
		setSize(250,50);
		repaint();
			
		showStatus("Player stopped");
	}
	
	private class PlayerHandler implements ControllerListener
	{
		public synchronized void controllerUpdate(ControllerEvent ce)
		{
			if( ce instanceof RealizeCompleteEvent ) {
				Component control=player.getControlPanelComponent();
			
				if( control != null )
					add(control,BorderLayout.NORTH);
				
				setSize(300,100);
				repaint();
			}
		}
	}	
}