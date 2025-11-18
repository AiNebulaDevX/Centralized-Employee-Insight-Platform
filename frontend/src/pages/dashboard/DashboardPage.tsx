import { Box, Container, Typography } from '@mui/material'

const DashboardPage = () => {
  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" gutterBottom>
        Dashboard
      </Typography>
      <Box sx={{ mt: 4 }}>
        <Typography>Welcome to the Employee Insights Platform</Typography>
      </Box>
    </Container>
  )
}

export default DashboardPage
