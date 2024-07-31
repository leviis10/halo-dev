package enigma.halodev.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConvertMultipartToFileTests {
    @Test
    public void ConvertMultipartToFile_testConvert() throws IOException {
        // given
        MultipartFile mockMultipartFile = Mockito.mock(MultipartFile.class);
        String originalFilename = "testfile.txt";
        byte[] fileContent = "This is a test file content.".getBytes();

        when(mockMultipartFile.getOriginalFilename()).thenReturn(originalFilename);
        when(mockMultipartFile.getBytes()).thenReturn(fileContent);

        // when
        File resultFile = ConvertMultipartToFile.convert(mockMultipartFile);

        // then
        // verify that file created
        assertTrue(resultFile.exists());
        assertTrue(resultFile.length() > 0);
        assertTrue(resultFile.getName().equals(originalFilename));

        // delete the created file after test
        resultFile.delete();
    }
}
