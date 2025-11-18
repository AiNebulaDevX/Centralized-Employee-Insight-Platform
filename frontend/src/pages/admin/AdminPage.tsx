import { Box, Container, Typography, Paper } from '@mui/material'

const AdminPage = () => {
  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" gutterBottom>
        Admin Dashboard
      </Typography>
      <Paper sx={{ p: 4, mt: 2 }}>
        <Typography>Admin content goes here</Typography>
      </Paper>
    </Container>
  )
}

export default AdminPage
