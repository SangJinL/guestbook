package kr.ac.pool.guestbook.service;

import kr.ac.pool.guestbook.dto.GuestBookDTO;
import kr.ac.pool.guestbook.dto.PageRequestDTO;
import kr.ac.pool.guestbook.dto.PageResultDTO;
import kr.ac.pool.guestbook.entity.GuestBook;
import kr.ac.pool.guestbook.repository.GuestBookRepositoty;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestBookServiceImpl implements GuestBookService{
    private final GuestBookRepositoty repositoty;
    @Override
    public Long register(GuestBookDTO dto) {
        GuestBook entity = dtoToEntity(dto);
        log.info(entity);
        repositoty.save(entity);

        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO) {
        PageRequest pageable = requestDTO.getPageable(Sort.by("gno").descending());
        Page<GuestBook> result = repositoty.findAll(pageable);
        Function<GuestBook, GuestBookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public GuestBookDTO read(Long gno) {
        Optional<GuestBook> result = repositoty.findById(gno);
        return result.isPresent()? entityToDto(result.get()): null;
    }

    @Override
    public void modify(GuestBookDTO dto) {
        Optional<GuestBook> result = repositoty.findById(dto.getGno());
        if(result.isPresent()){
            GuestBook entity = result.get();
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());
            repositoty.save(entity);
        }
    }

    @Override
    public void remove(Long gno) {
        repositoty.deleteById(gno);
    }
}
