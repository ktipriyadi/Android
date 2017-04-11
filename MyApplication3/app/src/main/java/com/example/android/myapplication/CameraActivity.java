package com.example.android.myapplication;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * Created by Muhamad Ichwan on 11/30/2016.
 */

public class CameraActivity extends AppCompatActivity{

    private Size previewsize;
    private Size jpegSize[]=null;

    private TextureView textureView;
    private Button getPicture;
    private CameraDevice cameraDevice;

    private CaptureRequest.Builder previewBuilder;
    private CameraCaptureSession previewSession;

    private static final SparseIntArray ORIENTATIONS=new SparseIntArray();

    static
    {
        ORIENTATIONS.append(Surface.ROTATION_0,90);
        ORIENTATIONS.append(Surface.ROTATION_90,0);
        ORIENTATIONS.append(Surface.ROTATION_180,270);
        ORIENTATIONS.append(Surface.ROTATION_270,180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textureView=(TextureView) findViewById(R.id.textureview);
        textureView.setSurfaceTextureListener(surfaceTextureListener);

        getPicture=(Button)findViewById(R.id.getpicture);
        getPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicture();
            }
        });
    }

    void getPicture()
    {
        if(cameraDevice==null)
        {
            return;
        }
        CameraManager manager=(CameraManager)getSystemService(Context.CAMERA_SERVICE);

        try
        {
            CameraCharacteristics characteristics=manager.getCameraCharacteristics(cameraDevice.getId());
            if (characteristics!=null)
            {
                jpegSize=characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width=640,height=480;
            if(jpegSize!=null&&jpegSize.length>0)
            {
                width=jpegSize[0].getWidth();
                height=jpegSize[0].getHeight();
            }
            ImageReader reader=ImageReader.newInstance(width,height,ImageFormat.JPEG,1);
            List<Surface> outputSurface = new ArrayList<Surface>(2);
            outputSurface.add(reader.getSurface());
            outputSurface.add(new Surface(textureView.getSurfaceTexture()));

            final CaptureRequest.Builder capturebuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);

            capturebuilder.addTarget(reader.getSurface());
            capturebuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            int rotation = getWindowManager().getDefaultDisplay().getRotation();

            capturebuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            final ImageReader.OnImageAvailableListener imageAvailableListener=new ImageReader.OnImageAvailableListener()
            {
                @Override
                public void onImageAvailable(ImageReader reader)
                {
                    Image image = null;
                    try
                    {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    }catch (Exception ee){

                    }
                    finally {
                        if(image!=null)
                            image.close();
                    }
                }

                void save(byte[] bytes)
                {
                    File file12 = getOutputMediaFile();
                    OutputStream outputStream = null;
                    try
                    {
                        outputStream = new FileOutputStream(file12);
                        outputStream.write(bytes);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }finally {
                        try {
                            if (outputStream != null)
                                outputStream.close();
                        }catch (Exception e){}
                    }
                }
            };

            HandlerThread handlerThread = new HandlerThread("takepicture");
            handlerThread.start();

            final Handler handler = new Handler(handlerThread.getLooper());
            reader.setOnImageAvailableListener(imageAvailableListener,handler);

            final CameraCaptureSession.CaptureCallback previewSSession = new CameraCaptureSession.CaptureCallback()
            {
                @Override
                public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber)
                {
                    super.onCaptureStarted(session, request, timestamp, frameNumber);
                }

                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    startCamera();
                }
            };

            cameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback()
            {
                @Override
                public void onConfigured(CameraCaptureSession session)
                {
                    try
                    {
                        session.capture(capturebuilder.build(),previewSSession,handler);
                    }catch (Exception e){}
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session)
                {

                }
            },handler);
        }catch (Exception e){}
    }

    public void openCamera()
    {
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try
        {
            String camerId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(camerId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            previewsize = map.getOutputSizes(SurfaceTexture.class)[0];
            try{
                manager.openCamera(camerId,stateCallback,null);
            }
            catch (SecurityException e)
            {

            }
        }catch (Exception e){}
    }

    private  TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener()
    {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height)
        {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height)
        {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface)
        {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface)
        {

        }
    };

    private CameraDevice.StateCallback stateCallback=new CameraDevice.StateCallback(){
        @Override
        public void onOpened(CameraDevice camera){
            cameraDevice=camera;
            startCamera();
        }

        @Override
        public void onDisconnected(CameraDevice camera){

        }

        @Override
        public void onError(CameraDevice camera, int error){

        }
    };

    @Override
    protected void onPause(){
        super.onPause();
        if(cameraDevice!=null){
            cameraDevice.close();
        }
    }

    void startCamera(){
        if(cameraDevice==null||!textureView.isAvailable()||previewsize==null){
            return;
        }

        SurfaceTexture texture=textureView.getSurfaceTexture();
        if(texture==null){
            return;
        }

        texture.setDefaultBufferSize(previewsize.getWidth(), previewsize.getHeight());

        Surface surface = new Surface(texture);

        try{
            previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        }catch(Exception e){

        }
        previewBuilder.addTarget(surface);
        try{
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(CameraCaptureSession session){
                    previewSession=session;
                    getChangedPreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session){

                }

            },null);
        }catch(Exception e){

        }
    }
    void getChangedPreview() {
        if (cameraDevice == null) {
            return;
        }
        previewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        HandlerThread thread = new HandlerThread("changed Preview");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        try {
            previewSession.setRepeatingRequest(previewBuilder.build(), null, handler);
        } catch (Exception e) {

        }
    }
        /*@Override
        public boolean onCreateOptionsMenu(Menu menu){
            super.getMenuInflater().inflate(R.menu.menu_main,menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item){
            int id = item.getItemId();

            if(id == R.id.action_settings){
                return true;
            }

            return super.onOptionsItemSelected(item);
        }*/

    private static File getOutputMediaFile()
    {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MyCameraApp");
        if (!mediaStorageDir.exists())
        {
            if(!mediaStorageDir.mkdirs())
            {
                Log.d("MyCameraApp","failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath()+File.separator+"IMG_"+timeStamp+".jpg");
        return mediaFile;
    }
}