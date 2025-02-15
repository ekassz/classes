library(shiny)
library(tidyr)
library(dplyr)
library(ggplot2)

diversity_tech <- read.csv("data/Diversity-in-tech-companies.csv")
View(diversity_tech)

diversity_tech <- diversity_tech %>%
  mutate(across(c(`Female..`, `Male..`, X..White, X..Asian, X..Latino, X..Black), as.numeric))

diversity_tech_category <- diversity_tech %>%
  pivot_longer(cols =  c(`X..White`, `X..Asian`, `X..Latino`, `X..Black`),
               names_to = "Ethnicity",
               values_to = "Percentage")
View(diversity_tech_category)

#renaming
diversity_tech_category <- diversity_tech_category %>%
  mutate(Ethnicity = case_when(
    Ethnicity == "X..White" ~ "White",
    Ethnicity == "X..Asian" ~ "Asian",
    Ethnicity == "X..Latino" ~ "Latino",
    Ethnicity == "X..Black" ~ "Black",
    TRUE ~ Ethnicity
  ))

ui <- fluidPage(
  titlePanel("Diversity in Tech Companies"),
  
  sidebarLayout(
    sidebarPanel(
      sliderInput("yearRange",
                  "Select Your Year Range:",
                  min = 2014, max = 2018,
                  value = c(2014, 2018),
                  sep = "")
    ),
    mainPanel(
      plotOutput("barPlot", brush = brushOpts(id = "plot_brush")),
      dataTableOutput("brushedData"),
      dataTableOutput("filteredData")
    )
  )
)

server <- function(input, output) {
  filtered_data <- reactive({
    diversity_tech_category %>%
      filter(Year >= input$yearRange[1] & Year <= input$yearRange[2]) %>%
      filter(Ethnicity %in% c("White", "Asian", "Latino", "Black")) %>%
      complete(Year, Ethnicity, Company, fill = list(Percentage = 0)) %>%
      select(-c(X..Multi, X..Other, X..Undeclared))
  })
  
  output$barPlot <- renderPlot({
    data <- filtered_data()
    
    ggplot(data, aes(x = Company, y = Percentage, fill = Ethnicity)) +
      geom_bar(stat = "identity", position = "dodge") +
      labs(
        title = "Ethnicity Representation in Tech Companies",
        x = "Company",
        y = "Percentage of Employees"
      ) +
      theme_minimal() +
      theme(legend.position = "right",
            axis.text.x = element_text(angle = 45, hjust = 1)) +
      scale_fill_brewer(palette = "Set3")
  })
  
  output$brushedData <- renderDataTable({
    brushed_data <- brushedPoints(filtered_data(), input$plot_brush)
    brushed_data
  })
  
  output$filteredData <- renderDataTable({
    filtered_data()
  })

}

# Run the Shiny app
shinyApp(ui = ui, server = server)
