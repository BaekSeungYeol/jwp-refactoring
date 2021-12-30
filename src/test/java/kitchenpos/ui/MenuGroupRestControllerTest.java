package kitchenpos.ui;

import kitchenpos.BaseControllerTest;
import kitchenpos.domain.Fixtures;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuGroupRestControllerTest extends BaseControllerTest {


    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다. 메뉴 그룹 등록 후, 아이디 정보를 반환한다.")
    void createMenuGroup() throws Exception {

        // given
        String name = "메뉴그룹1";
        MenuGroup menuGroup = Fixtures.menuGroup(name);

        // when - then
        mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroup)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(name));

    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    void menuGroupList() throws Exception {

        // when - then
        mockMvc.perform(get("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$..id").isNotEmpty())
                .andExpect(jsonPath("$..name").isNotEmpty());

    }
}