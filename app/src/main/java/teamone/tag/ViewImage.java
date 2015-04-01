package teamone.tag;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

import mpi.cbg.fly.Feature;
import mpi.cbg.fly.SIFT;

public class ViewImage extends ActionBarActivity {
    private static final int OK = 0;
    private static final int MEMORY_ERROR = 1;
    private static final int ERROR = 2;
    // Declare Variable
	TextView text;
	ImageView imageview;
    Bitmap mPicture;

    private ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		// Get the view from view_image.xml
		setContentView(R.layout.view_image);

        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar_view_image);
        setSupportActionBar(toolbar);

		// Retrieve data from MainActivity on GridView item click
		Intent i = getIntent();

		// Get the position
		int position = i.getExtras().getInt("position");

		// Get String arrays FilePathStrings
		String[] filepath = i.getStringArrayExtra("filepath");

		// Get String arrays FileNameStrings
		String[] filename = i.getStringArrayExtra("filename");

		// Locate the TextView in view_image.xml
		text = (TextView) findViewById(R.id.imagetext);

		// Load the text into the TextView followed by the position
		text.setText(filename[position]);

		// Locate the ImageView in view_image.xml
		imageview = (ImageView) findViewById(R.id.full_image_view);

		// Decode the filepath with BitmapFactory followed by the position
		Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);



        //sift
            // Free the data of the last picture
        if(mPicture != null)
            mPicture.recycle();
        mPicture = bmp;
        mPicture = mPicture.copy(Bitmap.Config.ARGB_8888, true);

		/////////////Pas Seulement sift //////////////
		// Set the decoded bitmap into ImageView
		imageview.setImageBitmap(mPicture);
        ///////////////////////////////////////////////

        Log.v("SIFT", "//////////////call Sift");
        processSIFT();

	}

    private void processSIFT() {
        Log.v("SIFT", "//////////////Process Sift");
        // show the dialog
        mProgressDialog = ProgressDialog.show(this, "Merci d'attendre",
                "Algo en cours");

        new Thread(new Runnable() {

            @Override
            public void run() {
                Message msg = null;
                msg = mHandler.obtainMessage();
                try {

                    // convert bitmap to pixels table
                    int pixels[] = toPixelsTab(mPicture);

                    // get the features detected into a vector
                    Vector<Feature> features = SIFT.getFeatures(
                            mPicture.getWidth(), mPicture.getHeight(), pixels);
                    Bitmap orig = mPicture;
                    // Immutable bitmap crash sinon - vu que les couleur sous android sans en ARGB et non pas RGB
                    mPicture = mPicture.copy(Bitmap.Config.ARGB_8888, true);


                    // draw features on bitmap
                    Canvas c = new Canvas(mPicture);
                    for (Feature f : features) {
                        drawFeature(c, f.location[0], f.location[1], f.scale,
                                f.orientation);
                    }

                    msg = mHandler.obtainMessage(OK);
                } catch (Exception e) {
                    e.printStackTrace();
                    msg = mHandler.obtainMessage(ERROR);
                } catch (OutOfMemoryError e) {
                    msg = mHandler.obtainMessage(MEMORY_ERROR);
                } finally {
                    // send the message
                    mHandler.sendMessage(msg);
                    Log.v("Handler ", msg +"");
                }
            }

        }).start();
    }

    private int[] toPixelsTab(Bitmap picture) {
        int width = picture.getWidth();
        int height = picture.getHeight();

        int[] pixels = new int[width * height];
        // copy pixels of picture into the tab
        picture.getPixels(pixels, 0, picture.getWidth(), 0, 0, width, height);


        // On Android, Color are coded in 4 bytes (argb),
        // whereas SIFT needs color coded in 3 bytes (rgb)

        for (int i = 0; i < (width * height); i++)
            pixels[i] &= 0x00ffffff;

        return pixels;
    }

    public void drawFeature(Canvas c, float x, float y, double scale,
                            double orientation) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // line too small...
        scale *= 6.;

        double sin = Math.sin(orientation);
        double cos = Math.cos(orientation);

        paint.setStrokeWidth(10f);
        paint.setColor(Color.BLUE);
        c.drawLine((float) x, (float) y, (float) (x - (sin - cos) * scale),
                (float) (y + (sin + cos) * scale), paint);

        paint.setStrokeWidth(20f);
        paint.setColor(Color.YELLOW);
        c.drawPoint(x, y, paint);
    }

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            AlertDialog.Builder builder;

            switch (msg.what) {
                case OK:
                    Log.v("msg.what.ok", "Show mPicture");
                    // set the picture with features drawed
                    imageview.setImageBitmap(mPicture);
                    break;
                case MEMORY_ERROR:
                    builder = new AlertDialog.Builder(ViewImage.this);
                    builder.setMessage("Out of memory.\nPicture too big.");
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                    break;
                case ERROR:
                    builder = new AlertDialog.Builder(ViewImage.this);
                    builder.setMessage("Error during the process.");
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                    break;
            }
            mProgressDialog.dismiss();
        }
    };

}