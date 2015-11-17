package com.alkaid.trip51.base.dataservice.http.impl;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.base.dataservice.FullRequestHandle;
import com.alkaid.trip51.base.dataservice.Request;
import com.alkaid.trip51.base.dataservice.RequestHandler;
import com.alkaid.trip51.base.dataservice.Response;
import com.alkaid.trip51.base.dataservice.http.FormInputStream;
import com.alkaid.trip51.base.dataservice.http.HttpRequest;
import com.alkaid.trip51.base.dataservice.http.HttpResponse;
import com.alkaid.trip51.base.dataservice.http.HttpService;
import com.alkaid.trip51.base.dataservice.http.NetworkInfoHelper;
import com.alkaid.trip51.util.MyTask;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public class DefaultHttpService
        implements HttpService {
    protected class Task extends MyTask<Void,Void,HttpResponse> implements WatchedInputStream.Listener {

        protected int availableBytes;
        protected int contentLength;
        protected final RequestHandler handler;
        protected boolean isUploadProgress;
        protected long prevProgressTime;
        protected int receivedBytes;
        protected final HttpRequest req;
        protected HttpUriRequest request;
        protected int sentBytes;
        protected long startTime;
        protected int statusCode;

        @Override
        public HttpResponse<Exception,byte[]> doInBackground(Void... params) {
            InputStream inputstream;
            inputstream = null;
            InputStream inputstream1 = req.input();
            if (inputstream1 != null && inputstream1.markSupported()) {
                inputstream1.mark(16384);
                inputstream = inputstream1;
            }
            HttpClient httpclient = null;
            org.apache.http.HttpResponse httpresponse;
            InputStream responseStream = null;
            HttpEntity httpentity;
            List<BasicNameValuePair> headers = null;
            BasicHttpResponse<Exception,byte[]> basichttpresponse = null;

            try {
                request = getUriRequest();
                httpclient = getHttpClient();
                httpresponse = httpclient.execute(request);
                statusCode = httpresponse.getStatusLine().getStatusCode();
                Header aheader[];
                httpentity = httpresponse.getEntity();
                headers = new ArrayList<BasicNameValuePair>();
                aheader = httpresponse.getAllHeaders();
                if (aheader.length > 0) {
                    for (int j = 0; j < aheader.length; j++) {
                        Header header = aheader[j];
                        headers.add(new BasicNameValuePair(header.getName(), header.getValue()));
                    }
                }
                if (httpentity != null) {
                    long l = httpentity.getContentLength();
                    ByteArrayBuffer bytearraybuffer;
                    byte buffer[];
                    long lastTime = 0L;
                    if (l > 0x7fffffffL || l < 0L)
                        l = -1L;
                    contentLength = (int) l;
                    receivedBytes = 0;
                    byte abyte0[];
                    byte abyte1[];
                    if (/*!"GET".equals(req.method()) ||*/ contentLength < 4096 || handler == null) {
                        abyte0 = EntityUtils.toByteArray(httpentity);
                        abyte1 = abyte0;
                    } else {
                        //                goto  _L5;else goto _L4
                        //                _L4:
                        bytearraybuffer = new ByteArrayBuffer(contentLength);
                        buffer = new byte[4096];
                        responseStream = httpentity.getContent();
                        for (int len=0;len != -1;len = responseStream.read(buffer) ) {
                            bytearraybuffer.append(buffer, 0, len);
                            receivedBytes = len + receivedBytes;
                            if (receivedBytes < contentLength) {/*goto _L9;else goto _L8*/
                                long curTime = SystemClock.elapsedRealtime();
                                if (curTime - lastTime > 200L) {
                                    publishProgress(new Void[0]);
                                    lastTime = curTime;
                                }
                            } else {
                                publishProgress(new Void[0]);
                            }
                        }
                        abyte1 = bytearraybuffer.toByteArray();

                    }
                    //                goto _L10
                    //                        ioexception1;
                    //                basichttpresponse = new BasicHttpResponse(-106, null, null, ioexception1);
                    httpentity.consumeContent();
                    basichttpresponse = new BasicHttpResponse(statusCode, abyte1, headers, null);
                } else {
                    basichttpresponse = new BasicHttpResponse(statusCode, null, headers, null);
                }
            } catch (Exception e) {
                LogUtil.e(e);
                int status = 0;
                if (e instanceof ConnectTimeoutException)
                    status = HTTP_ERROR_SENDING_REQUEST_CONNECTION_TIMEOUT;
                else if (e instanceof SocketTimeoutException)
                    status = HTTP_ERROR_SENDING_REQUEST_SOCKET_TIMEOUT;
                else if ((e instanceof UnknownHostException) || (e instanceof HttpHostConnectException))
                    status = HTTP_ERROR_CANNOT_SEND_REQUEST;
                else if (e instanceof IOException)
                    status = HTTP_ERROR_READ_STREAM;
                else {
                    status = HTTP_ERROR_SENDING_REQUEST;
                    logger(req, status, e);
                }
                basichttpresponse = new BasicHttpResponse(status, null, null, e);
            } finally {
                if (responseStream != null)
                    try {
                        responseStream.close();
                    } catch (IOException ioexception) {
                        ioexception.printStackTrace();
                    }
                if (httpclient != null)
                    recycleHttpClient(httpclient);
                if (inputstream != null)
                    try {
                        inputstream.reset();
                    } catch (Exception exception2) {
                    }
            }
            return basichttpresponse;
        }

        protected HttpUriRequest getUriRequest()
                throws Exception {
            Object obj;
            if ("GET".equals(req.method()))
                obj = new HttpGet(req.url());
            else if ("POST".equals(req.method())) {
                HttpPost httppost = new HttpPost(req.url());
                InputStream in = req.input();
                if (in != null) {
                    availableBytes = in.available();
                    sentBytes = 0;
                    if (availableBytes > 4096) {
//                        WatchedInputStream watchedinputstream = new WatchedInputStream(((InputStream) (obj1)), 4096);
//                        watchedinputstream.setListener(this);
//                        obj1 = watchedinputstream;
                        isUploadProgress = true;
                    }
                    httppost.setEntity(new InputStreamEntity(in, in.available()));
                }
                obj = httppost;
            } else if ("PUT".equals(req.method())) {
                HttpPut httpput = new HttpPut(req.url());
                InputStream in = req.input();
                if (in != null) {
                    availableBytes = in.available();
                    sentBytes = 0;
                    if (availableBytes > 4096) {
//                        WatchedInputStream watchedinputstream1 = new WatchedInputStream(((InputStream) (in)), 4096);
//                        watchedinputstream1.setListener(this);
//                        in = watchedinputstream1;
                        isUploadProgress = true;
                    }
                    httpput.setEntity(new InputStreamEntity(in, in.available()));
                }
                obj = httpput;
            } else if ("DELETE".equals(req.method()))
                obj = new HttpDelete(req.url());
            else
                throw new IllegalArgumentException((new StringBuilder()).append("unknown http method ").append(req.method()).toString());
            if (req.headers() != null) {
                NameValuePair namevaluepair;
                for (Iterator iterator = req.headers().iterator(); iterator.hasNext(); ((HttpUriRequest) (obj)).setHeader(namevaluepair.getName(), namevaluepair.getValue()))
                    namevaluepair = (NameValuePair) iterator.next();
            }
            if (req.timeout() > 0L) {
                org.apache.http.params.HttpParams httpparams = ((HttpUriRequest) (obj)).getParams();
                HttpConnectionParams.setSoTimeout(httpparams, (int) req.timeout());
                ((HttpUriRequest) (obj)).setParams(httpparams);
            }
            org.apache.http.HttpHost httphost = networkInfo.getProxy();
            ConnRouteParams.setDefaultProxy(((HttpUriRequest) (obj)).getParams(), httphost);
            return ((HttpUriRequest) (obj));
        }

        @Override
        public void notify(int remains) {
            if (handler != null && isUploadProgress) {
                sentBytes = remains + sentBytes;
                if (sentBytes >= availableBytes) {
                    publishProgress(new Void[0]);
                } else {
                    long l = SystemClock.elapsedRealtime();
                    if (l - prevProgressTime > 50L) {
                        publishProgress(new Void[0]);
                        prevProgressTime = l;
                    }
                }
            }
        }

        @Override
        protected void onCancelled() {
            if (isLoggable()) {
                long l = SystemClock.elapsedRealtime() - startTime;
                StringBuilder stringbuilder = new StringBuilder();
                stringbuilder.append("abort (");
                stringbuilder.append(req.method()).append(',');
                stringbuilder.append(statusCode).append(',');
                stringbuilder.append(l).append("ms");
                stringbuilder.append(") ").append(req.url());
                log(stringbuilder.toString());
                if (req.input() instanceof FormInputStream) {
                    FormInputStream forminputstream = (FormInputStream) req.input();
                    log((new StringBuilder()).append("    ").append(forminputstream.toString()).toString());
                }
            }
            if (request != null)
                request.abort();
        }

        @Override
        protected void onPostExecute(HttpResponse httpresponse) {
            if (runningTasks.remove(req, this)) {
                if (httpresponse.result() != null)
                    handler.onRequestFinish(req, httpresponse);
                else
                    handler.onRequestFailed(req, httpresponse);
                if (isLoggable()) {
                    long l = SystemClock.elapsedRealtime() - startTime;
                    StringBuilder stringbuilder = new StringBuilder();
                    if (httpresponse.result() != null)
                        stringbuilder.append("finish (");
                    else
                        stringbuilder.append("fail (");
                    stringbuilder.append(req.method()).append(',');
                    stringbuilder.append(statusCode).append(',');
                    stringbuilder.append(l).append("ms");
                    stringbuilder.append(") ").append(req.url());
                    log(stringbuilder.toString());
                    if (req.input() instanceof FormInputStream) {
                        FormInputStream forminputstream = (FormInputStream) req.input();
                        log((new StringBuilder()).append("    ").append(forminputstream.toString()).toString());
                    }
                    if (httpresponse.result() == null)
                        log((new StringBuilder()).append("    ").append(httpresponse.error()).toString());
                }
            }
        }

        @Override
        protected void onPreExecute() {
            if (handler instanceof FullRequestHandle)
                ((FullRequestHandle) handler).onRequestStart(req);
            startTime = SystemClock.elapsedRealtime();
        }

        @Override
        protected void onProgressUpdate(Void... params) {
            if (isUploadProgress) {
                if (handler instanceof FullRequestHandle)
                    ((FullRequestHandle) handler).onRequestProgress(req, sentBytes, availableBytes);
            }else {
                if (handler instanceof FullRequestHandle)
                    ((FullRequestHandle) handler).onRequestProgress(req, receivedBytes, contentLength);
            }
        }

        public Task(HttpRequest httprequest, RequestHandler requesthandler) {
            super();
            req = httprequest;
            handler = requesthandler;
        }
    }


    private static final int HTTP_ERROR_CANNOT_SEND_REQUEST = -102;
    private static final int HTTP_ERROR_READ_STREAM = -106;
    private static final int HTTP_ERROR_SENDING_REQUEST = -105;
    private static final int HTTP_ERROR_SENDING_REQUEST_CONNECTION_TIMEOUT = -103;
    private static final int HTTP_ERROR_SENDING_REQUEST_SOCKET_TIMEOUT = -104;
    private static final HashSet LOGGED_EXCEPTIONS = new HashSet();
    private static final String TAG = "http";
    private Context context;
    private Executor executor;
    private final ConcurrentLinkedQueue httpClients = new ConcurrentLinkedQueue();
    private NetworkInfoHelper networkInfo;
    private final ConcurrentHashMap runningTasks = new ConcurrentHashMap();

    public DefaultHttpService(Context context1, Executor executor1) {
        context = context1;
        executor = executor1;
        networkInfo = new NetworkInfoHelper(context);
    }

    private HttpClient getHttpClient() {
        HttpClient httpclient = (HttpClient) httpClients.poll();
        if (httpclient == null)
            httpclient = createHttpClient();
        return httpclient;
    }

    /**
     * @deprecated Method logger is deprecated
     */

    private void logger(HttpRequest httprequest, int i, Exception exception) {
//        this;
//        JVM INSTR monitorenter;
//        Class class1 = exception.getClass();
//        if (!LOGGED_EXCEPTIONS.contains(class1)) {
//            LOGGED_EXCEPTIONS.add(class1);
//            (new Thread(new HttpExceptionLogger(context.getSharedPreferences(context.getPackageName(), 0).getString("dpid", null), httprequest, networkInfo.getNetworkInfo(), i, exception))).start();
//        }
//        this;
//        JVM INSTR monitorexit;
//        return;
//        Exception exception1;
//        exception1;
//        throw exception1;
    }

    private void recycleHttpClient(HttpClient httpclient) {
        if (httpClients.size() < 4)
            httpClients.add(httpclient);
    }

    @Override
    public void abort(Request request, RequestHandler requesthandler, boolean flag) {
        abort((HttpRequest) request, requesthandler, flag);
    }

    public void abort(HttpRequest httprequest, RequestHandler requesthandler, boolean flag) {
        Task task = (Task) runningTasks.get(httprequest);
        if (task != null && task.handler == requesthandler) {
            runningTasks.remove(httprequest, task);
            task.cancel(flag);
        }
    }

    /**
     * @deprecated Method close is deprecated
     */

//    public void close() {
//        this;
//        JVM INSTR monitorenter;
//    }

    protected HttpClient createHttpClient() {
        return new DefaultHttpClient();
    }

    protected Task createTask(HttpRequest httprequest, RequestHandler requesthandler) {
        return new Task(httprequest, requesthandler);
    }

    @Override
    public void exec(Request request, RequestHandler requesthandler) {
        exec((HttpRequest) request, requesthandler);
    }

    public void exec(HttpRequest httprequest, RequestHandler requesthandler) {
        Task task = createTask(httprequest, requesthandler);
        if ((Task) runningTasks.putIfAbsent(httprequest, task) == null)
            task.executeOnExecutor(executor, new Void[0]);
        else
            Log.e("http", "cannot exec duplicate request (same instance)");
    }

    @Override
    public Response execSync(Request request) {
        return execSync((HttpRequest) request);
    }

    public HttpResponse execSync(HttpRequest httprequest) {
        return createTask(httprequest, null).doInBackground();
    }

    protected boolean isLoggable() {
        return Log.isLoggable(TAG,Log.DEBUG);
    }

    protected void log(String s) {
        Log.d(TAG, s);
    }

    public int runningTasks() {
        return runningTasks.size();
    }


}
