package enigma.halodev.response;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class PageResponse<T> {
    private List<T> content;
    private Integer size;
    private Integer totalPages;
    private Integer page;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.size = page.getSize();
        this.totalPages = page.getTotalPages();
        this.page = page.getNumber();
    }
}
