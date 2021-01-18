package lesson10.testing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest(RemoteFileHandler.class)
public class RemoteFileHandlerTest {

    RemoteFileHandler handler;
    SettingsLoader loader;
    ResponseRepository repo;

    @Before
    public void before() {
        loader = mock(SettingsLoader.class);
        repo = mock(ResponseRepository.class);
        handler = new RemoteFileHandler(loader, repo);
    }

    @Test
    public void handleRequestEmptyMap() {
        when(loader.loadSettings()).thenReturn(Collections.emptyMap());
        handler.handleRequest();
        PowerMockito.verifyZeroInteractions(RemoteFileReadWriter.class);
    }

    @Test
    public void handleRequest() throws Exception {
        // в случае корректной мапы хочу один вызов writer.readFileToList и один repo.writeResult
        RemoteFileReadWriter writer = mock(RemoteFileReadWriter.class);

        when(loader.loadSettings()).thenReturn(Collections.singletonMap("1", "1"));
        PowerMockito.whenNew(RemoteFileReadWriter.class).withNoArguments().thenReturn(writer);

        handler.handleRequest();

        verify(writer).readFileToList(any());
        verify(repo).writeResult(any());
    }

    @Test
    public void handleRequestWithException() {
        // жду запись сообщения об ошибке в случае если было исключение
        when(loader.loadSettings()).thenThrow(new NullPointerException("123"));
        handler.handleRequest();
        verify(repo).writeError("123");

    }

    @Test
    public void handleResponseValidateFalse() throws Exception {
        // жду один вызов repo.writeError
        List<String> list = Arrays.asList("1", "2", "3");
        RemoteFileHandler spy = PowerMockito.spy(new RemoteFileHandler(loader, repo));

        PowerMockito.when(spy,"validate", list).thenReturn(false);

        spy.handleResponse(list);

        verify(repo).writeError(anyString());
    }

    @Test
    public void handleResponseValidateTrueWithResult() throws Exception {
        // жду один вызов repo.writeResult, возвращающий true, и один вызов writer.writeResponse
        List<String> list = Arrays.asList("1", "2", "3");
        RemoteFileHandler spy = PowerMockito.spy(new RemoteFileHandler(loader, repo));
        RemoteFileReadWriter writer = mock(RemoteFileReadWriter.class);

        PowerMockito.when(spy,"validate", list).thenReturn(true);
        PowerMockito.whenNew(RemoteFileReadWriter.class).withNoArguments().thenReturn(writer);
        when(repo.writeResult(anyList())).thenReturn(true);

        spy.handleResponse(list);

        verify(repo).writeResult(list);
        verify(writer).writeResponse();
    }

    @Test
    public void handleResponseValidateTrueWithoutResult() throws Exception {
        // жду один вызова repo.writeResult, возвращающий false, при этом не должен создаваться writer
        List<String> list = Arrays.asList("1", "2", "3");
        RemoteFileHandler spy = PowerMockito.spy(new RemoteFileHandler(loader, repo));

        PowerMockito.when(spy,"validate", list).thenReturn(true);
        when(repo.writeResult(anyList())).thenReturn(false);

        spy.handleResponse(list);

        verify(repo).writeResult(list);
        PowerMockito.verifyZeroInteractions(RemoteFileReadWriter.class);
    }

    @Test
    public void handleError() {
        // ожидаю false если входящий параметр null или пустой список, true - в противном случае
        Assert.assertFalse(handler.handleError(null));
        Assert.assertFalse(handler.handleError(Collections.emptyList()));
        Assert.assertTrue(handler.handleError(Arrays.asList("1", "2", "3")));

    }

    @Test(expected = IllegalArgumentException.class)
    public void handleErrorWithException() {
        // если в списке была строка "error", ожидаю исключение
        Assert.assertTrue(handler.handleError(Arrays.asList("1","error", "3")));

    }
}